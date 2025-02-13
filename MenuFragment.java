package com.example.guidelink;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.util.ArrayList;

public class MenuFragment extends Fragment {

    private ListView lvMenuOptions;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_menu, container, false);
        lvMenuOptions = view.findViewById(R.id.lvMenuOptions);

        ArrayList<String> menuOptions = new ArrayList<>();
        menuOptions.add("Update Account");
        menuOptions.add("My Bookings");
        menuOptions.add("Chats");
        menuOptions.add("Create Itinerary");
        menuOptions.add("View Guides");
        menuOptions.add("View Spots");
        menuOptions.add("Home");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, menuOptions);
        lvMenuOptions.setAdapter(adapter);


        // Handle item clicks
        lvMenuOptions.setOnItemClickListener((parent, view1, position, id) -> {
            // Implement your functionality here based on position
            switch (position) {
                case 0:
                    // Open Update Account Fragment
                    FragmentManager fm1 = getParentFragmentManager();
                    FragmentTransaction ft1 = fm1.beginTransaction();
                    ft1.replace(R.id.myframe,new EditUserProfileFragment());
                    ft1.commit();
                    break;
                case 1:
                    // Open My Bookings Fragment
                    FragmentManager fm2 = getParentFragmentManager();
                    FragmentTransaction ft2 = fm2.beginTransaction();
                    ft2.replace(R.id.myframe, new BookedUserFragment());
                    ft2.commit();
                    break;
                case 2:
                    // Open Chats Fragment
                    break;
                case 3:
                    // Open Create Itinerary Fragment
                    break;
                case 4:
                    // Open View Guides Fragment
                    FragmentManager fm5 = getParentFragmentManager();
                    FragmentTransaction ft5 = fm5.beginTransaction();
                    ft5.replace(R.id.myframe, new ViewGuideFragment());
                    ft5.commit();
                    break;
                case 5:
                    FragmentManager fm6 = getParentFragmentManager();
                    FragmentTransaction ft6 = fm6.beginTransaction();
                    ft6.replace(R.id.myframe,new ViewSpotFragment());
                    ft6.commit();
                    break;
                case 6:
                    FragmentManager fm7 = getParentFragmentManager();
                    FragmentTransaction ft7 = fm7.beginTransaction();
                    ft7.replace(R.id.myframe,new HomeFragment());
                    ft7.commit();
                    break;

            }
        });

        return view;
    }
}
