package model;

/**
 * Java Record for the users table.
 * Roles  : STUDENT, TEACHER (as required by the activity), plus ADMIN (default account).
 * Status : PENDING (default), ACTIVE, INACTIVE
 */
public record User(int id, String username, String password, String roles, String status) {
}
