package com.example.ppmtoolmobile;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ppmtoolmobile.dao.DaoHelper;
import com.example.ppmtoolmobile.model.Priority;
import com.example.ppmtoolmobile.model.Project;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ProjectFragment extends Fragment implements View.OnClickListener, MyRecyclerAdapter.OnProjectClickListener {

    private Button projectAddBtn1;
    private List<Project> projectList;
    private MyRecyclerAdapter adapter;

    private RecyclerView recyclerView;
    private TextView sortProjectsTextView,displayUserProjectCountTextView;
    private ProgressBar projectListLoadingProgressBar;
    private long projectId;
    private int projectCount;
    private DaoHelper daoHelper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_project, null);
        projectList = new ArrayList<>();
        daoHelper = new DaoHelper(getActivity().getApplicationContext());
        projectAddBtn1 = v.findViewById(R.id.projectAddBtn1);
        sortProjectsTextView = v.findViewById(R.id.sortProjectsTextView);
        displayUserProjectCountTextView = v.findViewById(R.id.displayUserProjectCountTextView);
        projectListLoadingProgressBar = v.findViewById(R.id.projectListLoadingProgressBar);


        recyclerView = v.findViewById(R.id.projectRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(ProjectFragment.this.getActivity()));



        projectAddBtn1.setOnClickListener(this);
        sortProjectsTextView.setOnClickListener(this);

        return v;

    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onStart() {
        super.onStart();
        loadProjects();


    }



    @Override
    public void onClick(View view) {
        if(view == projectAddBtn1) {
            Intent goToAddProjectIntent = new Intent(ProjectFragment.this.getActivity(), AddProjectActivity.class);
            startActivity(goToAddProjectIntent);
        } else if(view == sortProjectsTextView) {
            sortProjects();
        }
    }

    private void sortProjects() {
        // Initializing the popup menu and giving the reference as current context
        PopupMenu popupMenu = new PopupMenu(ProjectFragment.this.getActivity(), sortProjectsTextView);

        // Inflating popup menu from popup_menu.xml file
        popupMenu.getMenuInflater().inflate(R.menu.sort_menu, popupMenu.getMenu());

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.option_priority_high_low:
                        // sort function
                        break;
                    case R.id.option_priority_low_high:
                        // sort function
                        break;
                    case R.id.option_due_date_newest_to_oldest:
                        // sort function
                        break;
                    case R.id.option_due_date_oldest_to_newest:
                        // sort function
                        break;
                }


                return true;
            }
        });
        // Showing the popup menu
        popupMenu.show();
    }

    @Override
    public void onProjectClick(View view, int position) {
        Project project = projectList.get(position);
        projectId = projectList.get(position).getId();
        Toast.makeText(ProjectFragment.this.getActivity(), "Short clicked", Toast.LENGTH_SHORT).show();
//        Intent viewSchedule = new Intent(ProjectFragment.this.getActivity(), ViewSchedule.class);
//        viewSchedule.putExtra("title", project.getTitle());
//        viewSchedule.putExtra("description", project.getDescription());
//
//        startActivity(viewSchedule);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onProjectLongClick(View view, int position) {


        Project project = projectList.get(position);
        projectId = projectList.get(position).getId();



        PopupMenu popupMenu = new PopupMenu(ProjectFragment.this.getActivity(), sortProjectsTextView);

        // Inflating popup menu from popup_menu.xml file
        popupMenu.getMenuInflater().inflate(R.menu.project_menu, popupMenu.getMenu());

        popupMenu.setOnMenuItemClickListener(menuItem -> {
            switch (menuItem.getItemId()) {
                case R.id.option_delete:

                    deleteProject(position);
                    projectList = daoHelper.getAllProjects();
                    adapter.notifyDataSetChanged();
                    break;
                case R.id.option_edit:

                    Toast.makeText(ProjectFragment.this.getActivity(), String.valueOf(project), Toast.LENGTH_SHORT).show();
                    break;
            }
            return true;
        });
        // Showing the popup menu
        popupMenu.show();



    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void loadProjects() {

        projectListLoadingProgressBar.setVisibility(View.VISIBLE);
        projectList.clear();

        projectList = daoHelper.getAllProjects();

        projectCount = projectList.size();
        adapter = new MyRecyclerAdapter(ProjectFragment.this.getActivity(), projectList, this);
        projectListLoadingProgressBar.setVisibility(View.GONE);
        recyclerView.setAdapter(adapter);

        displayUserProjectCountTextView.setText("You currently have " + projectCount + " project(s)");
        adapter.notifyDataSetChanged();

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void deleteProject(int position) {
        new AlertDialog.Builder(ProjectFragment.this.getActivity())
                .setTitle("Delete entry")
                .setMessage("Are you sure you want to delete this entry?")
                .setPositiveButton(android.R.string.yes, (dialog, which) -> {
                    boolean res = daoHelper.deleteProjectById(projectList.get(position));

                    adapter.notifyDataSetChanged();
                    Toast.makeText(ProjectFragment.this.getActivity(), String.valueOf(res), Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton(android.R.string.paste_as_plain_text, (dialog, which) -> {
                    Toast.makeText(ProjectFragment.this.getActivity(), "Not deleted", Toast.LENGTH_SHORT).show();
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();


    }

}