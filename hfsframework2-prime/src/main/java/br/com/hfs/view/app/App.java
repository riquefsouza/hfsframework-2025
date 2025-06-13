package br.com.hfs.view.app;

import java.io.Serializable;
import java.util.Locale;

import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;

@Named
@SessionScoped
public class App implements Serializable {

	private static final long serialVersionUID = 1L;
	private String theme = "saga";
    private boolean darkMode = false;
    private String inputStyle = "outlined";
    private Locale locale = new Locale.Builder().setLanguage("pt").setRegion("BR").build();
    
    public Locale getLocale() {
		return locale;
	}

	public void setLocale(Locale locale) {
		this.locale = locale;
	}

	public String getTheme() {
        return theme;
    }

    public boolean isDarkMode() {
        return darkMode;
    }

    public void setDarkMode(boolean darkMode) {
        this.darkMode = darkMode;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public String getInputStyle() {
        return inputStyle;
    }

    public void setInputStyle(String inputStyle) {
        this.inputStyle = inputStyle;
    }

    public String getInputStyleClass() {
        return this.inputStyle.equals("filled") ? "ui-input-filled" : "";
    }

    public void changeTheme(String theme, boolean darkMode) {
        this.theme = theme;
        this.darkMode = darkMode;
    }

    public String getThemeImage() {
    	String result = getTheme();
    	switch (result) {
		case "nova-light":
			result = "nova.png";
			break;
		case "nova-colored":
			result = "nova-accent.png";
			break;
		case "nova-dark":
			result = "nova-alt.png";
			break;
		case "bootstrap4-blue-light":
            result = "bootstrap4-light-blue.svg";
            break;
        case "bootstrap4-blue-dark":
            result = "bootstrap4-dark-blue.svg";
            break;
        case "bootstrap4-purple-light":
            result = "bootstrap4-light-purple.svg";
            break;
        case "bootstrap4-purple-dark":
            result = "bootstrap4-dark-purple.svg";
            break;
        default:
            result += ".png";
			break;
		}
    	return result;
    }
    
}
