package com.itheima.mp.service.impl;

import com.itheima.mp.domain.po.Address;
import com.itheima.mp.mapper.AddressMapper;
import com.itheima.mp.service.IAddressService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 吴浩
 * @since 2024-01-26
 */
@Service
public class AddressServiceImpl extends ServiceImpl<AddressMapper, Address> implements IAddressService {
}
