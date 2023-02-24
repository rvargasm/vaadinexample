package com.example.application.form;

import com.example.application.model.Usuario;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.shared.Registration;

public class UsuarioForm extends FormLayout {
	
	private TextField firstname = new TextField("Nombre");
    private TextField lastname = new TextField("Primer apellido");
    private TextField email = new TextField("Correo");
    private Button save = new Button("Save");
    private Button delete = new Button("Delete");
    private Button close = new Button("Cancel");
	
	Binder<Usuario> binder = new BeanValidationBinder<>(Usuario.class);

	private Usuario usuario;

	public UsuarioForm() {
		addClassName("usuario-form");
		binder.bindInstanceFields(this);
		add(firstname,
			lastname,
			email,
			createButtonsLayout());
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
		binder.readBean(usuario);
	}

	private Component createButtonsLayout() {
		save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
		delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
		close.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
		
		save.addClickShortcut(Key.ENTER);
		close.addClickShortcut(Key.ESCAPE);
		
		save.addClickListener(event -> validateAndSave());
		delete.addClickListener(event -> fireEvent(new DeleteEvent(this,usuario)));
		close.addClickListener(event -> fireEvent(new CloseEvent(this)));

		binder.addStatusChangeListener(e -> save.setEnabled(binder.isValid()));
		
		return new HorizontalLayout(save, delete, close);
	}

	private void validateAndSave() {
		try {
			binder.writeBean(usuario); // (5)
			fireEvent(new SaveEvent(this, usuario)); // (6)
		} catch (ValidationException e) {
			e.printStackTrace();
		}
	}

//Events

	public static abstract class UsuarioFormEvent extends ComponentEvent<UsuarioForm> {
		private Usuario usuario;

		protected UsuarioFormEvent(UsuarioForm source, Usuario usuario) { // (1)
			super(source, false);
			this.usuario = usuario;
		}

		public Usuario getUsuario() {
			return usuario;
		}
	}

	public static class SaveEvent extends UsuarioFormEvent {
		SaveEvent(UsuarioForm source, Usuario usuario) {
			super(source, usuario);
		}
	}
	
	public static class DeleteEvent extends UsuarioFormEvent {
		DeleteEvent(UsuarioForm source, Usuario usuario) {
			super(source, usuario);
		}
	}
	
	public static class CloseEvent extends UsuarioFormEvent {
		CloseEvent(UsuarioForm source) {
			super(source, null);
		}
	}

	public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
			ComponentEventListener<T> listener) { // (2)
		return getEventBus().addListener(eventType, listener);
	}

}
