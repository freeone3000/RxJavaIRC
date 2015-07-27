package net.freeone3000.rxjavairc;

import net.freeone3000.rxjavairc.gui.UI;
import net.freeone3000.rxjavairc.net.IRCMessage;
import net.freeone3000.rxjavairc.net.Networking;
import rx.Observable;

import javax.swing.*;
import java.net.InetSocketAddress;

public class Main {
	public static void main(String[] args) {
		InetSocketAddress freenode = new InetSocketAddress("chat.freenode.net", 6667);

		UI ui = new UI();
		Networking networking = new Networking();

		SwingUtilities.invokeLater(ui::createUI);
		Observable<IRCMessage> message = networking.joinServer(freenode, ui.getMessageStream());
		ui.bind(message);
	}
}
