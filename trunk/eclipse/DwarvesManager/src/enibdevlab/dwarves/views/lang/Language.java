package enibdevlab.dwarves.views.lang;

public enum Language {
	FRENCH,
	ENGLISH;

	public static Language fromString(String str) {
		if(str.equals("en")){
			return ENGLISH;
		}else if(str.equals("fr")){
			return FRENCH;
		}
		else return Language.getDefaultLanguage();
	}
	
	public static Language getDefaultLanguage(){
		return ENGLISH;
	}
	
	public static String toString(Language lang){
		switch(lang){
			case ENGLISH:
				return "en";
			case FRENCH:
				return "fr";
		}
		return "en";
	}
	
}
