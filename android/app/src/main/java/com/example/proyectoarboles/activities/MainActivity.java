package com.example.proyectoarboles.activities;

import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.proyectoarboles.R;
import com.example.proyectoarboles.fragments.AdminUsuariosFragment;
import com.example.proyectoarboles.fragments.ArbolDetallesFragment;
import com.example.proyectoarboles.fragments.DashboardFragment;
import com.example.proyectoarboles.fragments.DetalleCentroFragment;
import com.example.proyectoarboles.fragments.FormularioCentroFragment;
import com.example.proyectoarboles.fragments.FormularioDispositivoFragment;
import com.example.proyectoarboles.fragments.FormularioUsuarioFragment;
import com.example.proyectoarboles.fragments.ListarArbolesFragment;
import com.example.proyectoarboles.fragments.ListarCentrosFragment;
import com.example.proyectoarboles.fragments.LoginFragment;
import com.example.proyectoarboles.model.Usuario;
import com.example.proyectoarboles.util.PermissionManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigation;
    private PermissionManager permissionManager;
    private OnBackPressedCallback onBackPressedCallback;

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
                showFragment(new AdminUsuariosFragment());
                return true;
            } else if (itemId == R.id.menu_login) {
                if (permissionManager.isLoggedIn()) {
                    permissionManager.clearSession();
                    actualizarMenuSegunPermisos();
                    Toast.makeText(this, "Sesion cerrada", Toast.LENGTH_SHORT).show();
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

        if (getIntent() != null && getIntent().getBooleanExtra("SHOW_LOGIN_FRAGMENT", false)) {
            showFragment(new LoginFragment());
            bottomNavigation.setSelectedItemId(R.id.menu_login);
        }

        actualizarMenuSegunPermisos();
        configurarBackPressCallback();
    }

    @Override
    protected void onResume() {
        super.onResume();
        actualizarMenuSegunPermisos();
    }

    public void actualizarMenuSegunPermisos() {
        boolean loggedIn = permissionManager.isLoggedIn();

        bottomNavigation.getMenu()
                .findItem(R.id.menu_usuarios)
                .setVisible(permissionManager.isAdmin());

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

    private void showFragmentWithBackStack(Fragment fragment, int menuItemId) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit();
        setNavSelected(menuItemId);
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
        showFragmentWithBackStack(ArbolDetallesFragment.newInstance(arbolId), R.id.menu_arboles);
    }

    public void navigateToFormularioCentro() {
        showFragmentWithBackStack(FormularioCentroFragment.newInstance(), R.id.menu_centros);
    }

    public void navigateToFormularioCentro(long centroId) {
        if (centroId == -1) {
            navigateToFormularioCentro();
            return;
        }
        showFragmentWithBackStack(FormularioCentroFragment.newInstance(centroId), R.id.menu_centros);
    }

    public void navigateToFormularioDispositivo(long dispositivoId, long centroId) {
        if (dispositivoId == -1 && centroId == -1) {
            showFragmentWithBackStack(FormularioDispositivoFragment.newInstance(), R.id.menu_centros);
            return;
        }
        showFragmentWithBackStack(
                FormularioDispositivoFragment.newInstance(dispositivoId, centroId),
                R.id.menu_centros
        );
    }

    public void navigateToFormularioUsuario() {
        showFragmentWithBackStack(FormularioUsuarioFragment.newInstance(), R.id.menu_usuarios);
    }

    public void navigateToFormularioUsuario(Usuario usuario) {
        if (usuario == null || usuario.getId() == null) {
            navigateToFormularioUsuario();
            return;
        }
        showFragmentWithBackStack(FormularioUsuarioFragment.newInstance(usuario), R.id.menu_usuarios);
    }

    private void setNavSelected(int itemId) {
        bottomNavigation.getMenu().findItem(itemId).setChecked(true);
    }

    private void configurarBackPressCallback() {
        onBackPressedCallback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
                    getSupportFragmentManager().popBackStack();
                    return;
                }

                Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
                if (currentFragment instanceof LoginFragment) {
                    showFragment(new DashboardFragment());
                    bottomNavigation.setSelectedItemId(R.id.menu_dashboard);
                    return;
                }

                setEnabled(false);
                getOnBackPressedDispatcher().onBackPressed();
                setEnabled(true);
            }
        };
        getOnBackPressedDispatcher().addCallback(this, onBackPressedCallback);
    }
}
