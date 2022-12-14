package gsq.booker.sys.service.impl;


import gsq.booker.sys.common.ServiceResultEnum;
import gsq.booker.sys.dao.AdminUserMapper;
import gsq.booker.sys.dao.ParaMapper;
import gsq.booker.sys.entity.AdminUser;
import gsq.booker.sys.entity.Para;
import gsq.booker.sys.service.AdminUserService;
import gsq.booker.sys.util.MD5Util;
import gsq.booker.sys.util.PageQueryUtil;
import gsq.booker.sys.util.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class AdminUserServiceImpl implements AdminUserService {

    @Autowired
    private AdminUserMapper adminUserMapper;

    @Autowired
    private ParaMapper paraMapper;

    @Override
    public String updateParams(Para record){
        Para temp = paraMapper.selectByPrimaryKey(record.getId());
        if (temp == null) {
            return ServiceResultEnum.DATA_NOT_EXIST.getResult();
        }
        temp.setIdx1(record.getIdx1());
        if (paraMapper.updateSelective(temp) > 0) {
            return ServiceResultEnum.SUCCESS.getResult();
        }
        return ServiceResultEnum.DB_ERROR.getResult();
    }
    @Override
    public PageResult getParaPage(PageQueryUtil pageUtil){
        List<Para> paras = paraMapper.findParaList(pageUtil);
        PageResult pageResult = new PageResult(paras,6,pageUtil.getLimit(),pageUtil.getPage());
        return pageResult;
    }
    @Override
    public Para getParaById(Integer id) {
        return paraMapper.selectByPrimaryKey(id);
    }
    @Override
    public AdminUser login(String userName, String password) {
        String passwordMd5 = MD5Util.MD5Encode(password, "UTF-8");
        return adminUserMapper.login(userName, passwordMd5);
    }

    @Override
    public AdminUser getUserDetailById(Integer loginUserId) {
        return adminUserMapper.selectByPrimaryKey(loginUserId);
    }

    @Override
    public Boolean updatePassword(Integer loginUserId, String originalPassword, String newPassword) {
        AdminUser adminUser = adminUserMapper.selectByPrimaryKey(loginUserId);
        //???????????????????????????????????????
        if (adminUser != null) {
            String originalPasswordMd5 = MD5Util.MD5Encode(originalPassword, "UTF-8");
            String newPasswordMd5 = MD5Util.MD5Encode(newPassword, "UTF-8");
            //???????????????????????????
            if (originalPasswordMd5.equals(adminUser.getLoginPassword())) {
                //????????????????????????
                adminUser.setLoginPassword(newPasswordMd5);
                if (adminUserMapper.updateByPrimaryKeySelective(adminUser) > 0) {
                    //?????????????????????true
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public Boolean updateName(Integer loginUserId, String loginUserName, String nickName) {
        AdminUser adminUser = adminUserMapper.selectByPrimaryKey(loginUserId);
        //???????????????????????????????????????
        if (adminUser != null) {
            //????????????????????????
            adminUser.setLoginUserName(loginUserName);
            adminUser.setNickName(nickName);
            if (adminUserMapper.updateByPrimaryKeySelective(adminUser) > 0) {
                //?????????????????????true
                return true;
            }
        }
        return false;
    }
}
