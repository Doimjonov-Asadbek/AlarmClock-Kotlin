package app.calc.alarmmaster.model;

public class SendVerify {
    String email;
    String verefyCode;

    public SendVerify(String email, String verefyCode) {
        this.email = email;
        this.verefyCode = verefyCode;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getVerefyCode() {
        return verefyCode;
    }

    public void setVerefyCode(String verefyCode) {
        this.verefyCode = verefyCode;
    }

}
