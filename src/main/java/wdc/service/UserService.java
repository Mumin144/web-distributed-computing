package wdc.service;

import wdc.model.User;

public interface UserService {
	public User findUserByEmail(String email);
	public void saveUser(User user);
	public User findUserByName (String name);
}