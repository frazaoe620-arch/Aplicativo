package com.exemplo.floating;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private static final int DRAW_OVER_OTHER_APP_PERMISSION_REQUEST_CODE = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Verifica se o app tem permissão para desenhar sobre outros
        checkOverlayPermission();
    }

    private void checkOverlayPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(this)) {
                // Se não tem, abre a tela de configurações do sistema
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:" + getPackageName()));
                startActivityForResult(intent, DRAW_OVER_OTHER_APP_PERMISSION_REQUEST_CODE);
                Toast.makeText(this, "Autorize a sobreposição para o painel funcionar", Toast.LENGTH_LONG).show();
            } else {
                // Se já tem, inicia o painel flutuante
                startFloatingService();
            }
        } else {
            // Versões antigas do Android não precisam pedir essa permissão
            startFloatingService();
        }
    }

    private void startFloatingService() {
        startService(new Intent(this, FloatingService.class));
        finish(); // Fecha a tela principal para não atrapalhar
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == DRAW_OVER_OTHER_APP_PERMISSION_REQUEST_CODE) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (Settings.canDrawOverlays(this)) {
                    startFloatingService();
                } else {
                    Toast.makeText(this, "Permissão negada. O app não pode abrir o painel.", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}

