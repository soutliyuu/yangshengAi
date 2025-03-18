package com.sp.yangshengai.service.impl;

import cn.hutool.extra.spring.SpringUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sp.yangshengai.mapper.BigtypeMapper;
import com.sp.yangshengai.pojo.entity.Bigtype;
import com.sp.yangshengai.pojo.entity.Smalltype;
import com.sp.yangshengai.pojo.entity.vo.BigTypeAndSmallVo;
import com.sp.yangshengai.service.BigtypeService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author soutliyuu
 * @since 2025-03-04
 */
@Service
public class BigtypeServiceImpl extends ServiceImpl<BigtypeMapper, Bigtype> implements BigtypeService {
    private SmalltypeServiceImpl getsmalltypeService() {
        return SpringUtil.getBean(SmalltypeServiceImpl.class);
    }
    @Override
    public List<BigTypeAndSmallVo> getAll() {
        List<Smalltype> smalltypes = this.getsmalltypeService().list();
        List<Bigtype> bigtypes = this.list();
        return bigtypes.stream().map(bigtype -> {
            BigTypeAndSmallVo bigTypeAndSmallVo = BigTypeAndSmallVo.builder()
                    .id(bigtype.getId())
                    .name(bigtype.getBName())
                    .smalltypes(smalltypes.stream()
                            .filter(smalltype -> smalltype.getBigTypeId().equals(bigtype.getId()))
                            .map(smalltype -> Smalltype.builder()
                                    .id(smalltype.getId())
                                    .sName(smalltype.getSName())
                                    .build())
                            .collect(Collectors.toList()))
                    .build();
            return bigTypeAndSmallVo;
        }).collect(Collectors.toList());

    }
}
