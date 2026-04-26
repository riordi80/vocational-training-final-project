package com.example.proyectoarboles.activities;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.proyectoarboles.R;
import com.example.proyectoarboles.fragments.AdminUsuariosFragment;
import com.example.proyectoarboles.fragments.ArbolDetallesFragment;
import com.example.proyectoarboles.fragments.DashboardFragment;
import com.example.proyectoarboles.fragments.DetalleCentroFragment;
import com.example.proyectoarboles.fragments.ListarArbolesFragment;
import com.example.proyectoarboles.fragments.ListarCentrosFragment;
import com.example.proyectoarboles.fragments.LoginFragment;
import com.example.proyectoarboles.util.PermissionManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigation;
    private PermissionManager permissionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        permissionManager = new PermissionManager(this);
        bottomNavigation = findViewById(R.id.bottom_navigation);

        bottomNavigation.setOnNavigationItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.menu_dashboard) {
                showFragment(new DashboardFragment());
                return true;
            } else if (itemId == R.id.menu_centros) {
                showFragment(new ListarCentrosFragment());
                return true;
            } else if (itemId == R.id.menu_arboles) {
                showFragment(ListarArbolesFragment.newInstance(-1L));
                return true;
            } else if (itemId == R.id.menu_usuarios) {
                // Solo visible para ADMIN, cargar fragment de administración de usuarios
                showFragment(new AdminUsuariosFragment());
                return true;
            } else if (itemId == R.id.menu_login) {
                if (permissionManager.isLoggedIn()) {
                    permissionManager.clearSession();
                    actualizarMenuSegunPermisos();
                    Toast.makeText(this, "Sesión cerrada", Toast.LENGTH_SHORT).show();
                } else {
                    showFragment(new LoginFragment());
                }
                return true;
            }
            return false;
        });

        if (savedInstanceState == null) {
            showFragment(new DashboardFragment());
            bottomNavigation.setSelectedItemId(R.id.menu_dashboard);
        }

        // Verificar si debemos mostrar el fragment de Login
        if (getIntent() != null && getIntent().getBooleanExtra("SHOW_LOGIN_FRAGMENT", false)) {
            showFragment(new LoginFragment());
            bottomNavigation.setSelectedItemId(R.id.menu_login);
        }

        actualizarMenuSegunPermisos();
    }

    @Override
    protected void onResume() {
        super.onResume();
        actualizarMenuSegunPermisos();
    }

    /**
     * Actualiza los ítems del menú según el estado de sesión y el rol del usuario.
     * Público para que LoginFragment pueda llamarlo tras un login exitoso.
     */
    public void actualizarMenuSegunPermisos() {
        boolean loggedIn = permissionManager.isLoggedIn();

        // Usuarios: solo visible para ADMIN
        bottomNavigation.getMenu()
                .findItem(R.id.menu_usuarios)
                .setVisible(permissionManager.isAdmin());

        // Login / Logout: cambia título e icono según estado de sesión
        android.view.MenuItem loginItem = bottomNavigation.getMenu().findItem(R.id.menu_login);
        if (loggedIn) {
            loginItem.setTitle("Logout");
            loginItem.setIcon(R.drawable.ic_nav_logout);
        } else {
            loginItem.setTitle("Login");
            loginItem.setIcon(R.drawable.ic_nav_login);
        }
    }

    private void showFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }

    public void navigateToDashboard() {
        showFragment(new DashboardFragment());
        setNavSelected(R.id.menu_dashboard);
    }

    public void navigateToListarCentros() {
        showFragment(new ListarCentrosFragment());
        setNavSelected(R.id.menu_centros);
    }

    public void navigateToDetalleCentro(long centroId) {
        showFragment(DetalleCentroFragment.newInstance(centroId));
        setNavSelected(R.id.menu_centros);
    }

    public void navigateToListarArboles(long centroId) {
        showFragment(ListarArbolesFragment.newInstance(centroId));
        setNavSelected(R.id.menu_arboles);
    }

    public void navigateToLogin() {
        showFragment(new LoginFragment());
        setNavSelected(R.id.menu_login);
    }

    public void navigateToArbolDetalles(long arbolId) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, ArbolDetallesFragment.newInstance(arbolId))
                .addToBackStack(null)
                .commit();
    }

    private void setNavSelected(int itemId) {
        bottomNavigation.getMenu().findItem(itemId).setChecked(true);
    }

    @Override
    public void onBackPressed() {
        // Si hay fragmentos en el back stack, pop el último
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack();
        } else {
            // Si estamos en el fragment de Login, ir al Dashboard
            Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
            if (currentFragment instanceof LoginFragment) {
                showFragment(new DashboardFragment());
                bottomNavigation.setSelectedItemId(R.id.menu_dashboard);
            } else {
                super.onBackPressed();
            }
        }
    }
}
