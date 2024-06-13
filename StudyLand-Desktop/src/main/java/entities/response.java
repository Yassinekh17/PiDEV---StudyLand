package entities;

public class response {
    private int idReponse;
    private String contenu;
    private int idQuestion;


    private status  status;

    public enum status  {
        ONE, ZERO;}

    public response(int idReponse, String contenu, int idQuestion, status  status) {
        this.idReponse = idReponse;
        this.contenu = contenu;
        this.idQuestion = idQuestion;
        this.status = status;
    }

    public response(String contenu, int idQuestion, status  status) {
        this.contenu = contenu;
        this.idQuestion = idQuestion;
        this.status = status;
    }

    public response(int idReponse, String contenu) {
        this.idReponse = idReponse;
        this.contenu = contenu;
    }

    public response(int idReponse) {


        this.idReponse = idReponse;
    }

    public response() {
    }

    public int getIdReponse() {
        return idReponse;
    }

    public String getContenu() {
        return contenu;
    }

    public int getIdQuestion() {
        return idQuestion;
    }

    public status  getStatus() {
        return status ;
    }

    public void setIdReponse(int idReponse) {
        this.idReponse = idReponse;
    }

    public void setContenu(String contenu) {
        this.contenu = contenu;
    }

    public void setIdQuestion(int idQuestion) {
        this.idQuestion = idQuestion;
    }

    public void setStatus(status  status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "response{" +
                "idReponse=" + idReponse +
                ", contenu='" + contenu + '\'' +
                ", idQuestion=" + idQuestion +
                ", status=" + status +
                '}';
    }
}
