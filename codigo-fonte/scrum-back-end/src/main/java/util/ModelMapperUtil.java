package util;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ModelMapperUtil {
	@Autowired
	private static ModelMapper modelMapper;

	public static <T> T map(Object objeto, Class<T> tipo) {
		return modelMapper.map(objeto, tipo);
	}
}
