package com.example.application.views.main;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.example.application.form.UsuarioForm;
import com.example.application.model.Usuario;
import com.example.application.service.IUsuarioService;
import com.example.application.service.UsuarioService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;

@PageTitle("Main")
@Route(value = "")
@CssImport("./shared-styles.css")
public class MainView extends VerticalLayout {

	private Grid<Usuario> grid = new Grid<>(Usuario.class);
	private UsuarioForm form;
	private IUsuarioService usuarioService;

	public MainView(IUsuarioService usuarioService) {
		this.usuarioService = usuarioService;
		addClassName("list-view");
		setSizeFull();
		configureGrid();

		form = new UsuarioForm();

		form.addListener(UsuarioForm.SaveEvent.class, this::saveUsuario);
		form.addListener(UsuarioForm.DeleteEvent.class, this::deleteUsuario);
		form.addListener(UsuarioForm.CloseEvent.class, e -> closeEditor());

		Div content = new Div(grid, form);
		content.addClassName("content");
		content.setSizeFull();

		add(getToolbar(), content);
		updateList();
		
		closeEditor();
		
//        sayHello.addClickListener(e -> {
//        	Usuario newUser = new Usuario(firstname.getValue(),lastname.getValue(),email.getValue());
//        	items.add(newUser);
//            Notification.show("Se agregó " + firstname.getValue() + lastname.getValue())
//            			.setDuration(5000);
//            
//        });
//        
//        
//        

	}

	private void configureGrid(/* List<Usuario> items */) {
		grid.addClassNames("usuario-grid");
		grid.setSizeFull();
		grid.setColumns("firstname", "lastname", "email");
		grid.getColumns().forEach(col -> col.setAutoWidth(true));
		grid.setHeight("300px");
		grid.asSingleSelect().addValueChangeListener(event -> editUsuario(event.getValue()));
	}

	public void editUsuario(Usuario usuario) {
		if (usuario == null) {
			closeEditor();
		} else {
			form.setUsuario(usuario);
			form.setVisible(true);
			addClassName("editing");
		}
	}

	private void closeEditor() {
		form.setUsuario(null);
		form.setVisible(false);
		removeClassName("editing");
	}

	private void updateList() {
		grid.setItems(usuarioService.findAll());
	}

	private void saveUsuario(UsuarioForm.SaveEvent event) {
		usuarioService.save(event.getUsuario());
		updateList();
		closeEditor();
	}

	private void deleteUsuario(UsuarioForm.DeleteEvent event) {
		usuarioService.delete(event.getUsuario());
		updateList();
		closeEditor();
	}

	private HorizontalLayout getToolbar() {
		Button addUsuarioButton = new Button("Add contact");
		addUsuarioButton.addClickListener(click -> addContact());
		HorizontalLayout toolbar = new HorizontalLayout(addUsuarioButton);
		toolbar.addClassName("toolbar");
		return toolbar;
	}

	void addContact() {
		grid.asSingleSelect().clear();
		editUsuario(new Usuario());
	}

}
