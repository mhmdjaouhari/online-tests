package models;

import java.io.Serializable;
import java.util.ArrayList;

public class Temp implements Serializable {
    String cne;
    int id_test;
    int minute;
    public static class TempReponse implements Serializable{
        int id_question;
        String value;

        public TempReponse() {}

        public TempReponse(int id_question, String value) {
            this.id_question = id_question;
            this.value = value;
        }

        public int getId_question() {
            return id_question;
        }

        public void setId_question(int id_question) {
            this.id_question = id_question;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return "TempQuestion{" +
                    "id_question=" + id_question +
                    ", value='" + value + '\'' +
                    '}';
        }
    }

    ArrayList<TempReponse> reponses = new ArrayList<>();

    public String getCne() {
        return cne;
    }

    public void setCne(String cne) {
        this.cne = cne;
    }

    public int getId_test() {
        return id_test;
    }

    public void setId_test(int id_test) {
        this.id_test = id_test;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public ArrayList<TempReponse> getReponses() {
        return reponses;
    }

    public void setReponses(ArrayList<TempReponse> reponses) {
        this.reponses = reponses;
    }

    public static ArrayList<TempReponse> toTempReponse(ArrayList<Reponse>reponses){
        ArrayList<TempReponse> tempReponses = new ArrayList<>();
        for(Reponse reponse:reponses){
            TempReponse tempReponse = new TempReponse();
            tempReponse.setId_question(reponse.getIdQuestion());
            tempReponse.setValue(reponse.getValue());
            tempReponses.add(tempReponse);
        }
        return tempReponses;

    }

    @Override
    public String toString() {
        return "Temp{" +
                "cne='" + cne + '\'' +
                ", id_test=" + id_test +
                ", minute=" + minute +
                ", questions=" + reponses +
                '}';
    }
}


