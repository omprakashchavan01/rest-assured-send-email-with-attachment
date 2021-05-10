import io.restassured.http.ContentType;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import static io.restassured.RestAssured.given;

public class SendEmailWithAttachment {
    static String access_token = "enter_your_google_access_token_here";

    public static void main(String[] args) throws MessagingException, IOException {
        MimeMessage mimeMessage = Util.createEmailWithAttachment(
                "enter_receiver_email_address",
                "enter_sender_email_address",
                "Hi",
                "Hi dude, this was easy",
                new File("enter_file_path_here_e.g._D:/temp.txt_supports_less_than_5mb"));

        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        mimeMessage.writeTo(buffer);

        given().
                baseUri("https://gmail.googleapis.com").
                basePath("/upload/gmail/v1").
                header("Authorization", "Bearer " + access_token).
                header("Content-Type", "message/rfc822").
                pathParam("userid", "enter_your_email_address_associated_with_your_client_app").
                body(buffer.toByteArray()).
                log().all().
        when().
                post("/users/{userid}/messages/send?uploadType=media").
        then().
                statusCode(200).
                contentType(ContentType.JSON).
                log().all();

    }
}
