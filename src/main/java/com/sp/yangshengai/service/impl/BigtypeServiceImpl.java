package com.sp.yangshengai.service.impl;

import cn.hutool.extra.spring.SpringUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
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
 *  膳食大类服务实现类
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

    private PlanSmallServiceImpl getPlanSmallService() {return SpringUtil.getBean(PlanSmallServiceImpl.class);}
    @Override

    /**
     *
     * @return 带查询出来的数据
     * //获取全部膳食大类夹带着它的小类
     */

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

    @Override
    //删除单个膳食大类并将该大类下的所有小类删除
    public void deleteBigType(Integer id) {
        removeById(id);
        List<Smalltype> list = getsmalltypeService().list(new LambdaQueryWrapper<Smalltype>().eq(Smalltype::getBigTypeId, id));
        getsmalltypeService().removeByIds(list);
        getPlanSmallService().removeByIds(list);
    }
}
