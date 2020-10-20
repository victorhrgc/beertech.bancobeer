package beertech.becks.api.model.converter;

import beertech.becks.api.model.UserRoles;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;

public class GeneralConverter {

	@ReadingConverter
	public static class UserRolesConverter implements Converter<String, UserRoles> {

		@Override
		public UserRoles convert(final String source) {
			return UserRoles.valueOf(source);
		}
	}

}
