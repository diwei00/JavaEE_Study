package com.example.demo.controller;

import com.example.demo.common.*;
import com.example.demo.entity.ArticleInfo;
import com.example.demo.entity.CommentInfo;
import com.example.demo.entity.Userinfo;
import com.example.demo.entity.vo.ArticleInfoVO;
import com.example.demo.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

@RestController
@RequestMapping("/art")
public class ArticleController {
    // redis阅读数锁前缀
    private final String ReadCountLock = "readCountLock:";
    @Autowired
    private ArticleService articleService;


    /**
     * 发布博客
     *
     * @param articleInfo
     * @param request
     * @return
     */
    @RequestMapping("/add")
    public AjaxResult add(ArticleInfo articleInfo, HttpServletRequest request) {
        // 非空校验
        if (articleInfo == null || !StringUtils.hasLength(articleInfo.getTitle()) ||
                !StringUtils.hasLength(articleInfo.getContent())) {
            return AjaxResult.fail(-1, "参数异常");
        }
        // 得到uid，设置到当前articleInfo对象中

        Userinfo userinfo = UserSessionTools.getLoginUser(request);
        articleInfo.setUid(userinfo.getId());
        articleInfo.setState(1);

        // 持久化，返回数据到前端
        int result = articleService.add(articleInfo);
        return AjaxResult.success(result);

    }

    /**
     * 用于博客修改页，查询指定用户博客
     *
     * @param id
     * @param request
     * @return
     */
    @RequestMapping("/getdetailbyid")
    public AjaxResult getDetailById(Integer id, HttpServletRequest request) {
        if (id == null || id <= 0) {
            return AjaxResult.fail(-1, "非法参数");
        }
        // 获得当前用户id
        Userinfo userinfo = UserSessionTools.getLoginUser(request);
        // 查询操作
        ArticleInfo articleInfo = articleService.getDetailById(id, userinfo.getId());
        return AjaxResult.success(articleInfo);
    }

    /**
     * 修改博客
     *
     * @param articleInfo
     * @param request
     * @return
     */
    @RequestMapping("/update")
    public AjaxResult update(ArticleInfo articleInfo, HttpServletRequest request) {
        // 非空判断
        if (articleInfo == null ||
                !StringUtils.hasLength(articleInfo.getTitle()) ||
                !StringUtils.hasLength(articleInfo.getContent())) {
            return AjaxResult.fail(-1, "参数有误");
        }
        // 获取用户登录uid(修改需要验证用户权限)
        Userinfo userinfo = UserSessionTools.getLoginUser(request);
        articleInfo.setUid(userinfo.getId());
        articleInfo.setUpdatetime(LocalDateTime.now());

        int result = articleService.update(articleInfo);
        return AjaxResult.success(result);

    }

    /**
     * 获得博客正文
     *
     * @param id
     * @return
     */
    @RequestMapping("/getdetail")
    public AjaxResult getDetail(Integer id) {
        if (id == null || id <= 0) {
            return AjaxResult.fail(-1, "参数非法");
        }
        ArticleInfoVO articleInfoVO = articleService.getDetail(id);
        return AjaxResult.success(articleInfoVO);
    }

    /**
     * 增加阅读量
     * 使用redis + ip限制防止恶意刷阅读量（60秒内只能增加一次）
     * @param id
     * @return
     */
    @RequestMapping("/increasercount")
    public AjaxResult increaseRCount(Integer id, HttpServletRequest request) {
        if (id == null || id <= 0) {
            return AjaxResult.fail(-1, "参数非法");
        }
        // 1.设置锁，成功：增加阅读量，失败：不增加
        // key: readCountLock:ip:aid   value: aid
        // 注意: 这里key需要细化到文章id，保证一个IP下的锁对所有文章都有效果
        String key = ReadCountLock + IpUtil.getIpAddr(request) + ":" + id;
        Integer result = null;
        if(RedisUtil.setKeyIfAbsent(key, id.toString(), 60, TimeUnit.SECONDS)) {
           // 加锁成功增加阅读量
            result = articleService.increaseRCount(id);
        }
        // 加锁失败，不进行任何操作
        return AjaxResult.success(result == null ? 0 : result);
    }

    /**
     * 获取登录用户文章
     */
    @RequestMapping("/mylist")
    public AjaxResult getListByUId(Integer pageSize, Integer pageIndex, HttpServletRequest request) {
        // 非空校验
        if (pageSize == null || pageSize <= 0) {
            pageSize = 6;
        }
        if (pageIndex == null || pageIndex <= 0) {
            pageIndex = 1;
        }
        // 分页公式
        int offsetValue = pageSize * (pageIndex - 1);

        Userinfo userinfo = UserSessionTools.getLoginUser(request);
        List<ArticleInfo> myBlogList = articleService.getListByUid(userinfo.getId(), pageSize, offsetValue);
        // 对正文进行截取
        for (ArticleInfo item : myBlogList) {
            item.setContent(StringTools.subString(item.getContent(), ApplicationVariable.MAX_LENGTH));
        }

        return AjaxResult.success(myBlogList);
    }

    /**
     * 删除用户文章
     */
    @RequestMapping("/del")
    public AjaxResult deleteById(Integer id, HttpServletRequest request) {
        if (id == null || id <= 0) {
            return AjaxResult.fail(-1, "参数错误");
        }
        Userinfo userinfo = UserSessionTools.getLoginUser(request);
        int result = articleService.deleteById(id, userinfo.getId());
        return AjaxResult.success(result);
    }

    /**
     * 主页分页功能
     */
    @RequestMapping("/getlistbypage")
    public AjaxResult getListByPage(Integer pageSize, Integer pageIndex) {
        // 初始化默认值
        if (pageSize == null || pageSize <= 0) {
            pageSize = 6;
        }
        if (pageIndex == null || pageIndex < 1) {
            pageIndex = 1;
        }
        // 计算offset值
        int offsetValue = pageSize * (pageIndex - 1);
        List<ArticleInfo> list = articleService.getListByPage(pageSize, offsetValue);

        // 并发截取正文
        list.stream().parallel().forEach((item) -> {
            item.setContent(StringTools.subString(item.getContent(), ApplicationVariable.MAX_LENGTH));
        });
        return AjaxResult.success(list);

    }

    @RequestMapping("/count")
    public AjaxResult getArtCount(HttpServletRequest request) {
        Userinfo userinfo = UserSessionTools.getLoginUser(request);
        int result = articleService.getArtCount(userinfo.getId());
        if (result == 0) {
            return AjaxResult.fail(-1, "用户未发表文章");
        }
        return AjaxResult.success(result);
    }

    @RequestMapping("/getcount")
    public AjaxResult getCount() {
        int result = articleService.getCount();
        return AjaxResult.success(result);
    }

    @RequestMapping("/save_draft")
    public AjaxResult saveDraft(ArticleInfo articleInfo, HttpServletRequest request) {
        if (articleInfo == null || !StringUtils.hasLength(articleInfo.getTitle())
                || !StringUtils.hasLength(articleInfo.getContent())
                || articleInfo.getState() != 0) {
            return AjaxResult.fail(-1, "参数错误");
        }

        int uid = UserSessionTools.getLoginUser(request).getId();
        articleInfo.setUid(uid);
        int result = articleService.saveDraft(articleInfo);
        return AjaxResult.success(result);
    }


    /**
     * 定时发布
     *
     * @param time 用户定时时间
     * @return
     */
    @RequestMapping("/time_add")
    public AjaxResult timedRelease(String time, ArticleInfo articleInfo, HttpServletRequest request) {
        // 非空校验
        if (!StringUtils.hasLength(time)) {
            return AjaxResult.fail(-1, "参数错误！");
        }
        // 转换String为LocalDateTime类型
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm");
        LocalDateTime userTime = LocalDateTime.parse(time, formatter);

        // 先存文章草稿，当时间一到修改文章状态码为发布
        // 存取之后需要获取文章id，当修改文章状态的时候需要使用
        Userinfo userinfo = UserSessionTools.getLoginUser(request);
        articleInfo.setUid(userinfo.getId());
        articleInfo.setState(0);
        articleService.add(articleInfo);
        // 向线程池中提交任务，检测时间
        ApplicationVariable.executorService.submit(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    if (LocalDateTime.now().isAfter(userTime)) {
                        // 修改时间和状态
                        articleService.modifState(articleInfo.getId(), LocalDateTime.now());
                        break;
                    }
                }
            }
        });
        return AjaxResult.success(1);
    }

    /**
     * 获取自己的文章目录，用于内容管理页
     * @param request
     * @return
     */
    @RequestMapping("/getMyCatalogue")
    public AjaxResult getMyCatalogue(HttpServletRequest request) {
        // 用户一定会登录
        Userinfo user = UserSessionTools.getLoginUser(request);
        if(user == null) {
            return AjaxResult.fail(-2, "用户未登录");
        }
        List<ArticleInfo> articleInfoList = articleService.getMyCatalogue(user.getId());
        return AjaxResult.success(articleInfoList);
    }

    /**
     * 获取所有文章目录，用于博客大厅页
     * @return
     */
    @RequestMapping("/getAllCatalogue")
    public AjaxResult getAllCatalogue() {
        List<ArticleInfo> list = articleService.getAllCatalogue();
        return AjaxResult.success(list);
    }
}
