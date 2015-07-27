package net.freeone3000.rxjavairc.gui;

import lombok.Getter;
import net.freeone3000.rxjavairc.net.IRCMessage;
import rx.Observable;
import rx.observables.SwingObservable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;

public class UI {
	private final JTextArea textArea;
	private final JTextField inputField;

	@Getter
	private final Observable<String> messageStream;

	public UI() {
		textArea = new JTextArea();
		inputField = new JTextField();

		messageStream = SwingObservable.fromPressedKeys(inputField)
				.filter(keys -> keys.size() == 1 && keys.contains(KeyEvent.VK_ENTER))
				.map(keys -> {
					String input = inputField.getText();
					inputField.setText("");
					return input;
				});
	}

	public JFrame createUI() {
		JFrame jframe = new JFrame("IRC Client");
		jframe.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		JPanel contentPane = new JPanel(new BorderLayout());
		contentPane.add(textArea, BorderLayout.CENTER);
		contentPane.add(inputField, BorderLayout.PAGE_END);

		jframe.setVisible(true);
		return jframe;
	}

	public void bind(Observable<IRCMessage> messagesToDisplay) {
		messagesToDisplay
				.map(message -> message.getUser() + "(" + message.getChannel() + "): " + message.getMessage() + "\r\n")
				.subscribe(textArea::append);
	}
}
