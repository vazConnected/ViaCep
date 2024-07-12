package vazconnected.ViaCep.dto;

public record DeleteCepOutput(
		String httpStatusCode,
		Long entriesDeleted) {
}
