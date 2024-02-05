package com.xuecheng.content.api;


import com.xuecheng.content.model.dto.SaveTeachplanDTO;
import com.xuecheng.content.model.vo.TeachplanVO;
import com.xuecheng.service.ITeachplanService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api("课程计划接口")
@RestController
public class TeachplanController {

    @Autowired
    private ITeachplanService teachplanService;

    @ApiOperation("查询课程计划树形结构")
    @GetMapping("/teachplan/{courseId}/tree-nodes")
    public List<TeachplanVO> getTreeNodes(@PathVariable Long courseId){
        return teachplanService.getTreeNodes(courseId);
    }

    @ApiOperation("课程计划创建或修改")
    @PostMapping("/teachplan")
    public void saveTeachplan(@RequestBody SaveTeachplanDTO teachplanDTO) {
        teachplanService.saveTeachplan(teachplanDTO);

    }
}
