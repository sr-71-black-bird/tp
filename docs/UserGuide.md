---
layout: page
title: User Guide
---

AddressBook Level 3 (AB3) is a **desktop app for managing contacts, optimized for use via a Command Line Interface** (CLI) while still having the benefits of a Graphical User Interface (GUI). If you can type fast, AB3 can get your contact management tasks done faster than traditional GUI apps.

* Table of Contents
{:toc}

--------------------------------------------------------------------------------------------------------------------

## Quick start

1. Ensure you have Java `17` or above installed in your Computer.<br>
   **Mac users:** Ensure you have the precise JDK version prescribed [here](https://se-education.org/guides/tutorials/javaInstallationMac.html).

1. Download the latest `.jar` file from [here](https://github.com/se-edu/addressbook-level3/releases).

1. Copy the file to the folder you want to use as the _home folder_ for your AddressBook.

1. Open a command terminal, `cd` into the folder you put the jar file in, and use the `java -jar addressbook.jar` command to run the application.<br>
   A GUI similar to the below should appear in a few seconds. Note how the app contains some sample data.<br>
   ![Ui](images/Ui.png)

1. Type the command in the command box and press Enter to execute it. e.g. typing **`help`** and pressing Enter will open the help window.<br>
   Some example commands you can try:

   * `list` : Lists all contacts.

   * `add n/John Doe p/98765432 e/johnd@example.com a/John street, block 123, #01-01` : Adds a contact named `John Doe` to the Address Book.

   * `delete 3` : Deletes the 3rd contact shown in the current list.

   * `clear` : Deletes all contacts.

   * `exit` : Exits the app.

1. Refer to the [Features](#features) below for details of each command.

--------------------------------------------------------------------------------------------------------------------

## Features

<div markdown="block" class="alert alert-info">

**:information_source: Notes about the command format:**<br>

* Words in `UPPER_CASE` are the parameters to be supplied by the user.<br>
  e.g. in `add n/NAME`, `NAME` is a parameter which can be used as `add n/John Doe`.

* Items in square brackets are optional.<br>
  e.g `n/NAME [t/TAG]` can be used as `n/John Doe t/friend` or as `n/John Doe`.

* Items with `…`​ after them can be used multiple times including zero times.<br>
  e.g. `[t/TAG]…​` can be used as ` ` (i.e. 0 times), `t/friend`, `t/friend t/family` etc.

* Parameters can be in any order.<br>
  e.g. if the command specifies `n/NAME p/PHONE_NUMBER`, `p/PHONE_NUMBER n/NAME` is also acceptable.

* Extraneous parameters for commands that do not take in parameters (such as `help`, `list`, `exit` and `clear`) will be ignored.<br>
  e.g. if the command specifies `help 123`, it will be interpreted as `help`.

* If you are using a PDF version of this document, be careful when copying and pasting commands that span multiple lines as space characters surrounding line-breaks may be omitted when copied over to the application.
</div>

### Viewing help : `help`

Shows a message explaining how to access the help page.

![help message](images/helpMessage.png)

Format: `help`


### Adding an owner: `addowner`

Adds an owner to PetLog.

Format: `addowner on/NAME ph/PHONE_NUMBER em/EMAIL ad/ADDRESS [ot/TAG]…​`

<div markdown="span" class="alert alert-primary">:bulb: **Tip:**
A person can have any number of tags (including 0)
</div>

Examples:
* `addowner on/John Doe ph/98765432 em/johnd@example.com ad/John street, block 123, #01-01`
* `addowner on/Betsy Crowe ot/friend em/betsycrowe@example.com ad/Newgate Prison ph/1234567 ot/criminal`

### Adding a pet under an owner: `addpet`

Format: `addpet oi/OWNER_INDEX pn/PET_NAME ps/SPECIES [pr/REMARKS]`

* Adds the pet with specified `NAME` and `SPECIES` (and optional remark) under the owner specified by `OWNER_INDEX`

Examples:
* `addpet oi/2 pn/Molly ps/Golden Retriever pr/cuddly` Adds a cuddly golden retriever called Molly under the second
    owner in the list of owners; Molly will have a remark that she is cuddly.
* `addpet oi/1 pn/Dave ps/Great Dane` Adds a great dane called Dave under the first owner on the list of owners.

### Update a remark of a pet: `update`

Format: `update oi/OWNER_INDEX pi/PET_INDEX pr/REMARKS`

* Sets the remark of the pet specified by `PET_INDEX` under the owner specified by `OWNER_INDEX`.

Examples:
* `update oi/1 pi/3 pr/aggressive` Updates the remark of the third pet listed under the first owner to be "aggressive". 

### Listing all persons : `list`

Shows a list of all owners and pets in PetLog.

Format: `list`

### Editing an owner : `editowner`

Edits an existing owner in PetLog.

Format: `editowner oi/OWNER_INDEX [on/NAME] [ph/PHONE] [em/EMAIL] [ad/ADDRESS] [t/TAG]…​`

* Edits the person at the specified `OWNER_INDEX`. The index refers to the index number shown in the displayed person list. The index **must be a positive integer** 1, 2, 3, …​
* At least one of the optional fields must be provided.
* Existing values will be updated to the input values.
* When editing tags, the existing tags of the person will be removed i.e adding of tags is not cumulative.
* You can remove all the person’s tags by typing `ot/` without specifying any tags after it.

Examples:
*  `editowner oi/1 ph/91234567 em/johndoe@example.com` Edits the phone number and email address of the 1st person to be `91234567` and `johndoe@example.com` respectively.
*  `editowner oi/2 on/Betsy Crower ot/` Edits the name of the 2nd person to be `Betsy Crower` and clears all existing tags.

### Locating persons by name: `find`

Finds owners or pets whose details match all the given keywords.

Format: `find [on/OWNER_NAME] [ph/PHONE] [em/EMAIL] [ad/ADDRESS] [ot/OWNER_TAG]…​ [oi/OWNER_INDEX] [pn/PET_NAME] [ps/SPECIES] [pr/REMARKS]`

* At least one of the optional fields must be provided.
* The search is case-insensitive. e.g `hans` will match `Hans`.
* Partial matches are displayed e.g. `Han` will match `Hans`.
* Owners/Pets matching all keywords will be returned (i.e. `AND` search).

Examples:
* `find on/Hans ps/Dog` returns pets that are `Dogs` whose owner's name contains `Hans`.
* `find ad/Serangoon` returns owners whose address contains `Serangoon`.
  ![[result for 'find ad/Toa Payoh']](images/findAdToaPayohResult.png)

### Deleting a person : `delete`

Deletes the specified person from the address book.

Format: `delete INDEX`

* Deletes the person at the specified `INDEX`.
* The index refers to the index number shown in the displayed person list.
* The index **must be a positive integer** 1, 2, 3, …​

Examples:
* `list` followed by `delete 2` deletes the 2nd person in the address book.
* `find Betsy` followed by `delete 1` deletes the 1st person in the results of the `find` command.

### Adding a service : `addservice`

Adds a service to the list of services.

Format: `addservice sn/SERVICE_NAME sp/SERVICE_PRICE`

* The service name must not match that of an existing service in the list (case-insensitive).
* The price must be a non-negative number with up to 2 decimal places.

Examples:
* `addservice sn/Ear Cleaning sp/12.50` adds Ear Cleaning as a service to the list with the price of 12.50.

### Deleting a service : `deleteservice`

Deletes a service from the list of services.

Format: `deleteservice sn/SERVICE_NAME`

* The service name must match that of an existing service in the list (case-insensitive).

Examples:
* `deleteservice sn/Ear Cleaning` deletes Ear Cleaning as a service from the list (if it exists).

### Clearing all entries : `clear`

Clears all entries from the address book.

Format: `clear`

### Exiting the program : `exit`

Exits the program.

Format: `exit`

### Saving the data

AddressBook data are saved in the hard disk automatically after any command that changes the data. There is no need to save manually.

### Editing the data file

AddressBook data are saved automatically as a JSON file `[JAR file location]/data/petlog.json`. Advanced users are welcome to update data directly by editing that data file.

<div markdown="span" class="alert alert-warning">:exclamation: **Caution:**
If your changes to the data file makes its format invalid, AddressBook will discard all data and start with an empty data file at the next run. Hence, it is recommended to take a backup of the file before editing it.<br>
Furthermore, certain edits can cause the AddressBook to behave in unexpected ways (e.g., if a value entered is outside of the acceptable range). Therefore, edit the data file only if you are confident that you can update it correctly.
</div>

### Archiving data files `[coming in v2.0]`

_Details coming soon ..._

--------------------------------------------------------------------------------------------------------------------

## FAQ

**Q**: How do I transfer my data to another Computer?<br>
**A**: Install the app in the other computer and overwrite the empty data file it creates with the file that contains the data of your previous AddressBook home folder.

--------------------------------------------------------------------------------------------------------------------

## Known issues

1. **When using multiple screens**, if you move the application to a secondary screen, and later switch to using only the primary screen, the GUI will open off-screen. The remedy is to delete the `preferences.json` file created by the application before running the application again.
2. **If you minimize the Help Window** and then run the `help` command (or use the `Help` menu, or the keyboard shortcut `F1`) again, the original Help Window will remain minimized, and no new Help Window will appear. The remedy is to manually restore the minimized Help Window.

--------------------------------------------------------------------------------------------------------------------

## Command summary

Action | Format, Examples
--------|------------------
**Add Owner** | `addowner on/NAME ph/PHONE_NUMBER em/EMAIL ad/ADDRESS [ot/TAG]…​` <br> e.g., `addowner on/John Doe ph/98765432 em/johnd@example.com ad/John street, block 123, #01-01`
**Add Pet** | `addpet oi/OWNER_INDEX pn/PET_NAME ps/SPECIES [pr/REMARKS]` <br> e.g., `addpet oi/2 pn/Molly ps/Golden Retriever pr/cuddly`
**Add Service** | `addservice sn/SERVICE_NAME sp/SERVICE_PRICE` <br> e.g., `addservice sn/Ear Cleaning sp/12.50`
**Clear** | `clear`
**Delete** | `delete INDEX`<br> e.g., `delete 3`
**Delete Service** | `deleteservice sn/SERVICE_NAME` <br> e.g., `deleteservice sn/Ear Cleaning`
**Edit Owner** | `editowner oi/OWNER_INDEX [on/NAME] [ph/PHONE] [em/EMAIL] [ad/ADDRESS] [t/TAG]…​`<br> e.g., `editowner oi/1 ph/91234567 em/johndoe@example.com`
**Find** | `find [on/OWNER_NAME] [ph/PHONE] [em/EMAIL] [ad/ADDRESS] [ot/OWNER_TAG]…​ [oi/OWNER_INDEX] [pn/PET_NAME] [ps/SPECIES] [pr/REMARKS]`<br> e.g., `find on/Hans ps/Dog`
**List** | `list`
**Help** | `help`
