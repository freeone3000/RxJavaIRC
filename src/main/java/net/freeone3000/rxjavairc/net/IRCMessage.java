package net.freeone3000.rxjavairc.net;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class IRCMessage {
	private final String server;
	private final String user;
	private final String channel;
	private final String message;
}
