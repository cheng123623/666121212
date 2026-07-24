package com.sky.service;

import com.sky.entity.AddressBook;
import java.util.List;

public interface AddressBookService {
    List<AddressBook> list();
    void save(AddressBook addressBook);
    void update(AddressBook addressBook);
    void deleteById(Long id);
    void setDefault(AddressBook addressBook);
}
