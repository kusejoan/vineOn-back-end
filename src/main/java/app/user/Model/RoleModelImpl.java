package app.user.Model;

import app.user.Entity.Role;
import app.user.Repo.RoleRepository;
import org.springframework.stereotype.Service;

@Service
public class RoleModelImpl implements RoleModel{
    private RoleRepository roleRepository;

    public RoleModelImpl(RoleRepository roleRepository)
    {
        this.roleRepository = roleRepository;
    }
    @Override
    public Role getRoleByName(String name)
    {
        return roleRepository.findByName(name);
    }
}
