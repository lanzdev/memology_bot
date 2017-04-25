package com.lanzdev.managers.mysql.implementation;

import com.lanzdev.dao.entity.AdminDao;
import com.lanzdev.dao.mysql.implementation.MySqlAdminDao;
import com.lanzdev.managers.entity.AdminManager;
import com.lanzdev.model.entity.Admin;

import java.util.List;

public class MySqlAdminManager implements AdminManager {

    private AdminDao dao = new MySqlAdminDao();

    @Override
    public Admin add(Admin object) {
        return dao.create(object);
    }

    @Override
    public void update(Admin object) {
        dao.update(object);
    }

    @Override
    public Admin getById(Integer id) {
        return dao.get(id);
    }

    @Override
    public List<Admin> getAll( ) {
        return dao.getAll();
    }
}
