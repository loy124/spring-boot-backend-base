package com.loy124.myapp.core.util.mail;


import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class MailForm {
    private String from;
    private String to;
    private String subject;
    private String text;
    private String html;


    @Builder
    public MailForm(String from, String to, String subject, String text, String html) {
        this.from = from;
        this.to = to;
        this.subject = subject;
        this.text = text;
        this.html = html;
    }

    public static String buildHTMLContent(String randomPassword) {
        return "<!DOCTYPE html>" +
                "<html>" +
                "<head>" +
                "<meta charset=\"UTF-8\">" +
                "<title>임시 비밀번호 안내</title>" +
                "</head>" +
                "<body style=\"font-family: Arial, sans-serif; background-color: #f4f4f4;\">" +
                "<div style=\"width: 80%; margin: auto; text-align: center;\">" +
                "<h2 style=\"color: #333;\">mincourse 임시 비밀번호 안내</h2>" +
                "<p style=\"color: #666;\">새로 발급된 임시 비밀번호는 아래와 같습니다:</p>" +
                "<p style=\"color: #ff6600;\"><strong>" + randomPassword + "</strong></p>" +
                "<p style=\"color: #666;\">로그인 후 비밀번호를 변경해주시기 바랍니다.</p>" +
                "<p style=\"color: #666;\">감사합니다.</p>" +
                "</div>" +
                "</body>" +
                "</html>";
    }
}
