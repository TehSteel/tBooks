package dev.tehsteel.tbooks.common.user;

import lombok.Data;

@Data
public final class UserObject {
	private final String name;
	private final String email;
	private final UserRole userRole;
}
