package simulator;

import javafx.beans.value.ChangeListener;
import javafx.scene.control.CheckBox;

public class ValueClass {

	private CheckBox text_checkbox;
	private String text_pcl;
	private String text_code;
	private String text_zusatz;
	
	
	public ValueClass(CheckBox checkBox, String text_pcl, String text_code, String text_zusatz)
	{
		this.text_checkbox = checkBox;
		this.text_pcl = text_pcl;
		this.text_code = text_code;
		this.text_zusatz = text_zusatz;
	}
	

	public String getText_pcl() {
		return text_pcl;
	}

	public String getText_code() {
		return text_code;
	}

	public String getText_zusatz() {
		return text_zusatz;
	}
	
	public CheckBox getText_checkbox(){
		return text_checkbox;
	}

	@Override
	public String toString() {
		return "ValueClass [text_pcl=" + text_pcl + ", text_code=" + text_code
				+ ", text_zusatz=" + text_zusatz + "]";
	}
	
}
