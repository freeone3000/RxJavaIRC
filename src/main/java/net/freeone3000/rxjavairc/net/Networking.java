package net.freeone3000.rxjavairc.net;

import rx.Observable;
import rx.observables.StringObservable;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class Networking {
	public Observable<IRCMessage> joinServer(InetSocketAddress address, Observable<String> toWrite) {
		Observable<String> lines = Observable.<String, Socket>using(
				() -> {
					Socket socket = new Socket();
					try {
						socket.connect(address);
					} catch(IOException ioe) {
						throw new RuntimeException(ioe); //will be propagated
					}
					return socket;
				},
				socket -> {
					try {
						OutputStream outStream = socket.getOutputStream();
						InputStream inStream = socket.getInputStream();
						toWrite.<String>subscribe(data -> {
							try {
								outStream.write(data.getBytes(StandardCharsets.UTF_8));
							} catch(IOException ioe) {
								throw new RuntimeException(ioe);
							}
						});

						return StringObservable.byLine(StringObservable.decode(StringObservable.from(inStream), StandardCharsets.UTF_8));
					} catch(IOException ioe) {
						throw new RuntimeException(ioe);
					}
				},
				socketToDispose -> {
					try {
						socketToDispose.close();
					} catch(IOException ioe) {
						throw new RuntimeException(ioe); //will be propagated
					}
				}
		);

		return lines.filter(str -> !(str == null || str.isEmpty())).map(this::parseLine);
	}

	public IRCMessage parseLine(final String line) {
		return new IRCMessage("", "", "", line);
	}
}
