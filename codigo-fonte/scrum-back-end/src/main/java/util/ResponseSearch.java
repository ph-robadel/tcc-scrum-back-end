package util;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResponseSearch<T> {

	private List<T> lista;
	private Long total;
}
