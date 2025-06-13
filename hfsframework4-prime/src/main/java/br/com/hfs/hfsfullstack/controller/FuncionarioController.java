package br.com.hfs.hfsfullstack.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

import br.com.hfs.base.BaseViewRegisterPaged;
import br.com.hfs.hfsfullstack.model.Funcionario;
import br.com.hfs.hfsfullstack.service.FuncionarioService;

@Controller
@RequestMapping(value = "/private/admin/funcionarioView")
@SessionAttributes("listMenu")
public class FuncionarioController
		extends BaseViewRegisterPaged<Funcionario, Long, FuncionarioService> {

	private static final long serialVersionUID = 1L;

	public FuncionarioController() {
		super("/private/admin/funcionario/listFuncionario",
				"/private/admin/funcionario/editFuncionario", 
				"Funcionario", Funcionario.class);
	}

}
