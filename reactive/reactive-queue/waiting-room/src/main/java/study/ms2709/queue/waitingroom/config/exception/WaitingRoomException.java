package study.ms2709.queue.waitingroom.config.exception;

import ms2709.study.exception.StudyError;
import org.jetbrains.annotations.NotNull;

public class WaitingRoomException extends StudyError {

	private WaitingRoomException(@NotNull String message) {
		super(message);
	}

	public static WaitingRoomException build(ErrorType errorType, Object... args) {
		return new WaitingRoomException(errorType.getMessage().formatted(args));
	}


	public enum ErrorType {
		ALREADY_REGISTERED_USER("already registered user(%s)");

		private String message;


		ErrorType(String message) {
			this.message = message;
		}

		public String getMessage() {
			return message;
		}
	}

}
