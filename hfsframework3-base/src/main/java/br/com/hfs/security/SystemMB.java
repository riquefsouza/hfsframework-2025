package br.com.hfs.security;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.hfs.ApplicationConfig;
import br.com.hfs.ApplicationUtil;
import br.com.hfs.admin.model.AdmUser;
import br.com.hfs.admin.service.AdmUserService;
import br.com.hfs.admin.service.TestModeService;
import br.com.hfs.admin.vo.AuthenticatedUserVO;
import br.com.hfs.admin.vo.MenuVO;
import br.com.hfs.admin.vo.PageVO;
import br.com.hfs.admin.vo.PermissionVO;
import br.com.hfs.admin.vo.UserVO;
import br.com.hfs.base.BaseViewController;
import br.com.hfs.util.bcrypt.BCryptUtil;
import br.com.hfs.util.interceptors.HandlingExpectedErrors;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;

@Named
@ViewScoped
@HandlingExpectedErrors
public class SystemMB extends BaseViewController implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;
	
	private static Logger log = LoggerFactory.getLogger(SystemMB.class);
/*
	@Autowired
	private LdapUtil ldapUtil;
*/	
	@Autowired
	private ApplicationConfig applicationConfig;
	
	@Autowired
	private ApplicationUtil applicationUtil;
	
	//@Autowired
	private AdmUserService admUserService;

	@Autowired
	protected AuthenticatedUserVO authenticatedUser;
	
	@Autowired
	private TestModeService testModeService;
	
	public SystemMB() {
		admUserService = new AdmUserService();
	}
	
	public void secureSession() {
		log.warn(this.authenticatedUser.getDisplayName() + " remains connected in the session");
	}
	
	public boolean authenticate(String login, String password) throws Exception {
		UserVO userVO;
		Optional<UserVO> func;
		
		if (login.trim().isEmpty() || password.trim().isEmpty()) {
			return false;
		}
		
		if (testModeService.habilitarSenhaTeste()){
			func = testModeService.autenticarViaSenhaTeste(login, password);
			
			if (func.isPresent()){
				//String ldapDN = "CN="+login+",OU="+func.get().getSetor()+",OU=TRT,DC=trtrio,DC=gov,DC=br";
				String ldapDN = "CN="+login+",OU=DISAD,OU=TRT,DC=trtrio,DC=gov,DC=br";
				userVO = new UserVO(func.get().getId(), func.get().getEmail(),
						ldapDN, login, func.get().getName(), true);
				setProperties(login, userVO);
				return true;
			}
		}
		
		/*
		if (autenticarViaLDAP(login, password)) {
			userVO = new UserVO(ldapUtil.getMatricula(), ldapUtil.getEmail(), 
					ldapUtil.getLdapDN(), ldapUtil.getLogin(), ldapUtil.getNome());
			setProperties(login, userVO);
			return true;
		}
		*/
		
		Optional<AdmUser> user = admUserService.findByLogin(login);
		if (user.isPresent()) {			
			String hashed = user.get().getPassword();
			if (BCryptUtil.verifyAndUpdateHash(password, hashed, BCryptUtil.update)) {
				setProperties(login, user.get().toUserVO());
				return true;
			}
		}
		
		return false;
	}
	
	private void setProperties(String login, UserVO userVO) throws Exception {
		this.authenticatedUser.setUserName(login);
		if (applicationConfig.isEnableProfileControl()) {
			
			//admUserService.setAdmProfileService(admProfileService);
			/*
			AdmFuncionarioDTO funcionario;
			Long matricula = userVO.getMatricula();
			
			if (ldapUtil.isCpf()){
				BigDecimal cpf = new BigDecimal(matricula);
				funcionario = admUserService.findByCPF(cpf);
			} else {
				funcionario = admUserService.load(matricula);
			}
			
			if (funcionario != null) {
			*/
				this.authenticatedUser = testModeService.mountAuthenticatedUser(login, 
						userVO, this.authenticatedUser);
			//}
			
			this.authenticatedUser = testModeService.iniciar(this.authenticatedUser);
			
			if (this.authenticatedUser.isModeTest()
					&& !this.authenticatedUser.getModeTestLoginVirtual().isEmpty()){
				
				this.authenticatedUser.setUser(admUserService.getUser(
						this.authenticatedUser.getUser().getId(),
						this.authenticatedUser.getUserName(),
						this.authenticatedUser.getDisplayName(),
						//this.authenticatedUser.getFuncionario().getCpf(),
						this.authenticatedUser.getEmail(), 
						userVO.getLdapDN(), false).toUserVO());											
				
			} else {			
				this.authenticatedUser.setUser(admUserService.getUser(							
						this.authenticatedUser.getUser().getId(), 
						userVO.getLogin(), userVO.getName(),
					//this.authenticatedUser.getFuncionario().getCpf(),
					userVO.getEmail(), userVO.getLdapDN(), true).toUserVO());											
			}
			
			applicationUtil.setAuthenticatedUser(this.authenticatedUser);

			
			log.info(this.authenticatedUser.getUserName() + ", Profiles: "
					+ this.authenticatedUser.getListPermission().toString());
			mostrarPerfilURL();
			mostrarMenus();
		}
	}
	
	public void mostrarPerfilURL() {
		for (PermissionVO permission : this.authenticatedUser.getListPermission()) {
			for (PageVO admPage : permission.getPages()) {
				log.info("Perfil: " + permission.getProfile().getDescription() + " -> Pagina URL: " + admPage.getUrl());
			}
		}
	}
	
	public void mostrarMenus() {		
		for (MenuVO menu : this.authenticatedUser.getListMenus()) {
			log.info("Menu: " + menu.toString());
		}
		for (MenuVO menu : this.authenticatedUser.getListAdminMenus()) {
			log.info("Menu Admin: " + menu.toString());
		}		
	}
	
	public List<MenuVO> getListMenus() {
		return this.authenticatedUser.getListMenus();
	}

	public List<MenuVO> getListAdminMenus() {
		return this.authenticatedUser.getListAdminMenus();
	}


	public PageVO getPagina(Long idMenu) {
		return this.authenticatedUser.getPageByMenu(idMenu);
	}

	/*
	private boolean autenticarViaLDAP(String login, String senha) {
		if (!login.isEmpty() && !senha.isEmpty()) {
			LdapConfig config = new LdapConfig();				
			ldapUtil.configurar(config);
			
			if (login.endsWith(".esta")){ //Estagiarios
				config.setLdapBaseDN(config.getLdapBaseDN2());
			} else {
				config.setLdapBaseDN(config.getLdapBaseDN1());
			}
			
			ldapUtil.configurar(config);
			log.info(config.toString());
			ldapUtil.setListaLdapAttribute(ldapUtil.getAttributes(login));
			
			//return ldapLogin.login(login, senha);
			return ldapUtil.login(login, senha);				
		}

		return false;
	}
	*/

	/* (non-Javadoc)
	 * @see br.jus.trt1.hfsframework.base.BaseViewController#getAuthenticatedUser()
	 */
	public AuthenticatedUserVO getAuthenticatedUser() {
		return authenticatedUser;
	}
}
