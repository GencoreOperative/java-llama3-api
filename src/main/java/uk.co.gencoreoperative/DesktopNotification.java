package uk.co.gencoreoperative;

import java.awt.*;
import java.awt.TrayIcon.MessageType;
import java.io.IOException;

import com.sshtools.twoslices.Slice;
import com.sshtools.twoslices.Toast;
import com.sshtools.twoslices.ToastActionListener;
import com.sshtools.twoslices.ToastType;

public class DesktopNotification {
    public static void main(String[] args) throws IOException, InterruptedException {
        Toast.builder()
                .title("Hello")
                .content("World")
                .toast();
        Slice information = Toast.toast(ToastType.WARNING, "Information", "Here is some information you cannot do without.");

    }
}
