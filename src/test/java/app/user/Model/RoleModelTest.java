package app.user.Model;

import app.user.Repo.RoleRepository;
import org.junit.Test;

import static org.mockito.Mockito.*;

public class RoleModelTest {
    private RoleRepository roleRepository = mock(RoleRepository.class);
    private RoleModel roleModel = new RoleModelImpl(roleRepository);

    @Test
    public void checkRoleName()
    {
        roleModel.getRoleByName("regular");
        verify(roleRepository,times(1)).findByName("regular");
    }
}
