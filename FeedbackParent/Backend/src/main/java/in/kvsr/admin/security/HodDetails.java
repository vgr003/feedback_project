package in.kvsr.admin.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import in.kvsr.common.entity.HOD;



public class HodDetails implements UserDetails {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private HOD hod;
	
	public HodDetails(HOD hod) {
		this.hod = hod;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		List<SimpleGrantedAuthority> authorities = new ArrayList<>();
		authorities.add(new SimpleGrantedAuthority("HOD"));
		return authorities;
	}

	@Override
	public String getPassword() {
		return hod.getPassword();
	}

	@Override
	public String getUsername() {
		return hod.getRegId();
	}

	@Override
	public boolean isAccountNonExpired() {
		return hod.isEnabled();
	}

	@Override
	public boolean isAccountNonLocked() {
		return hod.isEnabled();
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return hod.isEnabled();
	}

	@Override
	public boolean isEnabled() {
		return hod.isEnabled();
	}

}
