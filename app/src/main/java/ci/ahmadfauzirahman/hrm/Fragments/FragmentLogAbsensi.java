package ci.ahmadfauzirahman.hrm.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.facebook.shimmer.ShimmerFrameLayout;

import java.util.List;

import ci.ahmadfauzirahman.hrm.Adapters.AbsenAdapter;
import ci.ahmadfauzirahman.hrm.Model.LogAbsensiModel;
import ci.ahmadfauzirahman.hrm.R;
import ci.ahmadfauzirahman.hrm.Response.LogAbsensiResponse;
import ci.ahmadfauzirahman.hrm.Rest.ApiClient;
import ci.ahmadfauzirahman.hrm.Rest.ApiInterface;
import ci.ahmadfauzirahman.hrm.Utils.SessionManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentLogAbsensi#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentLogAbsensi extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    View view;

    RecyclerView recyclerView;
    SwipeRefreshLayout swipeRefreshLayout;
    private String TAG = this.getClass().getName();
    SessionManager sessionManager;
    String kode;
    ApiInterface apiService =
            ApiClient.getClient().create(ApiInterface.class);
    Intent intent;
    private ShimmerFrameLayout mShimmerViewContainer;

    public FragmentLogAbsensi() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentLogAbsensi.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentLogAbsensi newInstance(String param1, String param2) {
        FragmentLogAbsensi fragment = new FragmentLogAbsensi();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_log_absensi, container, false);
        System.out.println("Ini Log Absensi");
        sessionManager = new SessionManager(getContext());
        kode = sessionManager.getUserDetail().get("username").toString();


        swipeRefreshLayout = view.findViewById(R.id.swpLogAbsensi);
        recyclerView = (RecyclerView) view.findViewById(R.id.reyLogAbsensi);
        mShimmerViewContainer = view.findViewById(R.id.shimmer_view_container);


        getAllLogAbsensi(kode);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // loading = ProgressDialog.show(context,null,"Sedang mendapatkan berita",true,false);
                swipeRefreshLayout.setRefreshing(false);
                getAllLogAbsensi(kode);

            }
        });
        return view;
    }

    private void getAllLogAbsensi(String kode) {
        final RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.reyLogAbsensi);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        Log.e(TAG, "Kode" + kode);

        apiService.list_absensi(kode).enqueue(new Callback<LogAbsensiResponse>() {
            @Override
            public void onResponse(Call<LogAbsensiResponse> call, Response<LogAbsensiResponse> response) {

                if (response.body().getCon()) {
                    Log.e(TAG, "OnResponse Url" + response.toString());
                    System.out.println("OnResponse Data" + response.body().toString());

                    if (response.isSuccessful()) {
                        List<LogAbsensiModel> logAbsensiModels = response.body().getResults();
                        recyclerView.setAdapter(new AbsenAdapter(logAbsensiModels, R.layout.list_log_absen, getContext()));
                        mShimmerViewContainer.stopShimmerAnimation();
                        mShimmerViewContainer.setVisibility(View.GONE);
                    } else {
                        System.out.println("OnResponse Data" + response.body().toString());
                        Log.e(TAG, "OnError" + response.body().toString());

                        Toast.makeText(getContext(), "Tidak Terhubung KeJaringan", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getContext(), "Data Not Found", Toast.LENGTH_SHORT).show();
                    mShimmerViewContainer.stopShimmerAnimation();
                    mShimmerViewContainer.setVisibility(View.GONE);
                }

            }

            @Override
            public void onFailure(Call<LogAbsensiResponse> call, Throwable t) {
                Log.e(TAG, "OnError " + t.getLocalizedMessage());
                System.out.println("Error Aplikasi" +
                        "" + t.getLocalizedMessage());
            }
        });
    }
}
