package simulator;

public class ValueClassSpeicher
{
	private String text_zahl;
	private String text_00;
	private String text_01;
	private String text_02;
	private String text_03;
	private String text_04;
	private String text_05;
	private String text_06;
	private String text_07;
	
	public ValueClassSpeicher(String text_zahl, String text_00, String text_01, String text_02, String text_03, String text_04, String text_05, String text_06, String text_07)
	{
		this.setText_zahl(text_zahl);
		this.setText_00(text_00);
		this.setText_01(text_01);
		this.setText_02(text_02);
		this.setText_03(text_03);
		this.setText_04(text_04);
		this.setText_05(text_05);
		this.setText_06(text_06);
		this.setText_07(text_07);
	}

	public String getText_zahl() {
		return text_zahl;
	}

	public void setText_zahl(String text_zahl) {
		this.text_zahl = text_zahl;
	}

	public String getText_00() {
		return text_00;
	}

	public void setText_00(String text_00) {
		this.text_00 = text_00;
	}

	public String getText_01() {
		return text_01;
	}

	public void setText_01(String text_01) {
		this.text_01 = text_01;
	}

	public String getText_02() {
		return text_02;
	}

	public void setText_02(String text_02) {
		this.text_02 = text_02;
	}

	public String getText_03() {
		return text_03;
	}

	public void setText_03(String text_03) {
		this.text_03 = text_03;
	}

	public String getText_04() {
		return text_04;
	}

	public void setText_04(String text_04) {
		this.text_04 = text_04;
	}

	public String getText_05() {
		return text_05;
	}

	public void setText_05(String text_05) {
		this.text_05 = text_05;
	}

	public String getText_06() {
		return text_06;
	}

	public void setText_06(String text_06) {
		this.text_06 = text_06;
	}

	public String getText_07() {
		return text_07;
	}

	public void setText_07(String text_07) {
		this.text_07 = text_07;
	}
	
	@Override
	public String toString() {
		return "ValueClass [text_zahl=" + text_zahl + ", text_00=" + text_00
				+ ", text_01=" + text_01
				+ ", text_02=" + text_02
				+ ", text_03=" + text_03
				+ ", text_04=" + text_04
				+ ", text_05=" + text_05
				+ ", text_06=" + text_06
				+ ", text_07=" + text_07
				+ "]";
	}
}
