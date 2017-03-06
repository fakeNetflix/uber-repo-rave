package com.uber.rave.sample.github;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.uber.rave.Rave;
import com.uber.rave.RaveException;
import com.uber.rave.sample.github.model.Owner;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * This uses the default Java serializer. This simulate storing the object to disk, but only to memory for
 * demo purposes.
 */
public class OwnerStorage {
    private byte[] serializedOwner = new byte[0];

    /**
     * "Store" the owner object.
     *
     * @param owner object to store.
     */
    public void storeOwner(Owner owner) {
        serializedOwner = serialize(owner);
    }

    /**
     * Get the "stored" owner object.
     *
     * @return "stored" owner object.
     */
    public Owner getOwner() {
        return deserialize(serializedOwner);
    }

    /**
     * Serialize a given Owner object into a byte array.
     * @param owner {@link Owner} object to serialize.
     * @return serialized owner as a byte array
     */
    private byte[] serialize(Owner owner) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            ObjectOutputStream out = new ObjectOutputStream(bos);
            out.writeObject(owner);
            out.flush();
            return bos.toByteArray();
        } catch (IOException ignored) {
            return new byte[0];
        } finally {
            try {
                bos.close();
            } catch (IOException ex) {
                // ignore close exception
            }
        }
    }

    /**
     * Deserialize a byte array to an Owner object.
     * @param bytes byte array to deserialize
     * @return {@link Owner} object deserialized from byte array
     */
    private Owner deserialize(byte[] bytes) {
        if (serializedOwner.length == 0) {
            return null;
        }

        ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
        ObjectInputStream in = null;

        try {
            in = new ObjectInputStream(bis);
            Owner deserializedOwner = (Owner) in.readObject();
            Rave.getInstance().validate(deserializedOwner);
            return deserializedOwner;
        } catch (ClassNotFoundException | IOException e) {
            return null;
        } catch (RaveException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                // ignore close exception
            }
        }
    }
}
