package com.if4b.aplikasiabsensikeretaapi;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.if4b.aplikasiabsensikeretaapi.model.ModelAbsensiMasuk;

import java.util.List;

public class AdapterHistory extends ArrayAdapter<ModelAbsensiMasuk> {

    TextView tvNama, tvJabatan, tvAlamat, tvTanggal;
    public AdapterHistory(Context context, List<ModelAbsensiMasuk> absenList) {
        super(context, 0, absenList);

    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.activity_list_item_history, parent, false);
        }

        ModelAbsensiMasuk currentAbsen = getItem(position);

        tvNama = convertView.findViewById(R.id.tv_nama_hist);
        tvJabatan = convertView.findViewById(R.id.tv_jabatan_hist);
        tvAlamat = convertView.findViewById(R.id.tv_alamat_hist);
        tvTanggal = convertView.findViewById(R.id.tv_tanggal_hist);

        tvNama.setText(currentAbsen.getNama());
        tvJabatan.setText(currentAbsen.getJabatan());
        tvAlamat.setText(currentAbsen.getLokasi());
        tvTanggal.setText(currentAbsen.getTanggal());

        return convertView;
    }
}
