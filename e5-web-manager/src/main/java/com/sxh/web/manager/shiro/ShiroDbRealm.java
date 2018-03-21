package com.sxh.web.manager.shiro;

import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;

public class ShiroDbRealm extends AuthorizingRealm{  
  
//	@Autowired
//    private UserMapper userMapper;
  
    /** 
     * 授权信息 :五表联查，找到用户对应权限
     */  
 protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {  
        String username=(String)principals.fromRealm(getName()).iterator().next();  
        if( username != null ){  
//            User user = accountManager.get( username );
//            if( user != null && user.getRoles() != null ){
//                SimpleAuthorizationInfo info = new SimpleAuthorizationInf();
//                for( SecurityRole each: user.getRoles() ){
//                        info.addRole(each.getName());
//                        info.addStringPermissions(each.getPermissionsAsString());
//                }
//                return info;
//            }
        	SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
            //去数据库中查询用户的角色和权限，注意是五表联查
            //把查询到的角色或权限，放在SimpleAuthorizationInfo对象中。这里静态代码模拟
        	//去数据库中查询用户的角色和权限
        	info.addRole("admin");
        	info.addStringPermission("addItem");
        	return info;
        } 
        return null;  
    }  
  
    /**
     * 认证信息 ：登录，验证用户名和密码
     */
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authcToken ){//每次登录时会进来
        UsernamePasswordToken token = (UsernamePasswordToken) authcToken;  
        String userName = token.getUsername();
        String password = new String(token.getPassword());
        //查询数据库，验证用户密码是否正确，这里静态代码模拟，admin/admin
        //User user = userMapper.findUserByUsernameAndPassword(token.getUsername(),password);
        if( userName != null && !"".equals(userName) ){  
            if(userName.equals("admin")&&password.equals("admin")){
                //md5加密+盐值（随机数），保证不会被破解。盐值是asdfad，转成二进制字节码
                return new SimpleAuthenticationInfo(userName,password, ByteSource.Util.bytes("asdfad"), getName());
            }
        }
        return null;  
    }  
  
}  
