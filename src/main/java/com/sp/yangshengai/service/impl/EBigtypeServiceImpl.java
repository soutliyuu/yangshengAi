package com.sp.yangshengai.service.impl;

import cn.hutool.extra.spring.SpringUtil;
import com.sp.yangshengai.pojo.entity.EBigtype;
import com.sp.yangshengai.mapper.EBigtypeMapper;
import com.sp.yangshengai.pojo.entity.ESmalltype;
import com.sp.yangshengai.pojo.entity.vo.EBigTypeAndSmallVo;
import com.sp.yangshengai.service.EBigtypeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sp.yangshengai.service.ESmalltypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author soutliyuu
 * @since 2025-03-05
 */
@Service
@RequiredArgsConstructor
public class EBigtypeServiceImpl extends ServiceImpl<EBigtypeMapper, EBigtype> implements EBigtypeService {

    private ESmalltypeServiceImpl getesmalltypeService() {
        return SpringUtil.getBean(ESmalltypeServiceImpl.class);
    }


    @Override
    public List<EBigTypeAndSmallVo> getAll() {
        List<ESmalltype> smalltypes = this.getesmalltypeService().list();
        List<EBigtype> bigtypes = this.list();
        return bigtypes.stream().map(bigtype -> {
            EBigTypeAndSmallVo bigTypeAndSmallVo = EBigTypeAndSmallVo.builder()
                    .id(bigtype.getId())
                    .name(bigtype.getEbName())
                    .eSmalltypes(smalltypes.stream()
                            .filter(smalltype -> smalltype.getEBigTypeId().equals(bigtype.getId()))
                            .map(smalltype -> ESmalltype.builder()
                                    .id(smalltype.getId())
                                    .esName(smalltype.getEsName())
                                    .build())
                            .collect(Collectors.toList()))
                    .build();
            return bigTypeAndSmallVo;
        }).collect(Collectors.toList());
    }


}






