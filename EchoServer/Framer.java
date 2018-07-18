import java.util.LinkedList;
import java.util.List;

/* Message Framer
 *
 * if there is no newline character, append received messages to buffer,
 * whenever encountering a newline character, move that line of characters
 * into messageQueue
 * */
public class Framer {
    String buffer = "";
    List<String> messageQueue = new LinkedList<>();

    /* parse newly received messages into Framer
    * if there are new lines, put them into messageQueue,
    * otherwise store it into buffer
    * */
    void addMessage(String msg) {
        char[] arr = msg.toCharArray();
        int n = arr.length, start = 0;
        for (int i = 0; i < n; i++) {
            if (arr[i] == '\n') {
                messageQueue.add(buffer + msg.substring(start, i));
                buffer = "";
                start = i + 1;
            }
        }
        if (start < n) {
            buffer += msg.substring(start, n);
        }
    }

    /* pop out messageQueue, return all the lines stored so far */
    List<String> popMessages() {
        List<String> msgs = new LinkedList<>();
        msgs.addAll(messageQueue);
        messageQueue = new LinkedList<>();
        return msgs;
    }

    /* whether there is lines of messages */
    boolean hasMessages() {
        return messageQueue.size() > 0;
    }
}
