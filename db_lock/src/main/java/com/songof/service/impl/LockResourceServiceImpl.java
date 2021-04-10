package com.songof.service.impl;

import com.songof.entity.LockResource;
import com.songof.mapper.LockResourceMapper;
import com.songof.service.LockResourceService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * @author SongOf
 * @ClassName LockResourceServiceImpl
 * @Description
 * @Date 2021/4/10 16:41
 * @Version 1.0
 */
@Service
public class LockResourceServiceImpl implements LockResourceService {

    @Resource
    private LockResourceMapper lockResourceMapper;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean lock(LockResource lockResource) {
        LockResource lr = lockResourceMapper.selectForUpdate(lockResource.getResourceName());
        if (lr != null && lr.getId() != null) {
            if (lr.getNodeInfo().equals(lockResource.getNodeInfo())) {
                System.out.println(lr.getNodeInfo()+lockResource.getNodeInfo());
                lockResourceMapper.updateCount(lr.getId());
                return true;
            }
        } else {
            lockResourceMapper.insertLockResource(lockResource);
            return true;
        }
        return false;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean unlock(LockResource lockResource) {
        LockResource lr = lockResourceMapper.select(lockResource.getId());
        if (lr != null && lr.getId() != null) {
            if(lr.getNodeInfo().equals(lockResource.getNodeInfo())){
                if(lr.getCount()>1){
                    lockResourceMapper.updateCountDecrease(lockResource.getId());
                }else{
                    lockResourceMapper.deleteLockResource(lockResource.getId());
                }
                return true;
            }
        }
        return false;
    }
}
