package com.example.anavia.examenfinal

import android.content.Context
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat.getSystemService
import android.support.v7.app.AlertDialog
import android.widget.Toast
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), LocationListener, OnMapReadyCallback{


    var lm: LocationManager?=null
    var map: GoogleMap?=null

    var name:String? = null
    var description:String?=null
    var latitude =-33.4528965
    var longitude =-70.6865687
    var marcador =LatLng(latitude,longitude)


    val myDB = CustomSQL(this,"myDB", null,1)


    override fun onMapReady(p0: GoogleMap?) {
        map = p0

        val permissions = arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION,
            android.Manifest.permission.ACCESS_FINE_LOCATION)

        var granted = true
        for (cadaPermiso in permissions){
            granted = granted and (ActivityCompat.checkSelfPermission(this, cadaPermiso)== PackageManager.PERMISSION_GRANTED)
        }
        if (!granted){
            ActivityCompat.requestPermissions(this,permissions,1)
        }else{
            p0?.isMyLocationEnabled = true
        }
    }

    override fun onLocationChanged(location: Location?) {

        //name = location?.name.toString()
        longitude = location?.longitude.toString().toDouble()
        latitude = location?.latitude.toString().toDouble()

        lblLongitude.text = location?.longitude.toString()
        lblLatitude.text = location?.latitude.toString()

        marcador =LatLng(latitude,longitude)

    }

    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onProviderEnabled(provider: String?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onProviderDisabled(provider: String?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {

        when(requestCode){
            1->{
                lm = getSystemService(Context.LOCATION_SERVICE)as LocationManager
                var granted = true
                for (permiso in permissions){
                    granted = granted and (ActivityCompat.checkSelfPermission(
                        this,
                        permiso
                    ) == PackageManager.PERMISSION_GRANTED)
                }
                if(grantResults.size>0 && granted){
                    lm?.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                        100,
                        1f,
                        this)
                }else {
                    Toast.makeText(this,"Permiso de GPS requerido", Toast.LENGTH_LONG).show()
                }
            }
        }}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        lm = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        lblLongitude.text = longitude.toString()
        lblLatitude.text = latitude.toString()

        val fragmentoMapa = supportFragmentManager.findFragmentById(R.id.frgMyMap) as SupportMapFragment
        fragmentoMapa.getMapAsync(this)

        btnAdd.setOnClickListener {
            Toast.makeText(this,"Inicio agregar lugar favorito", Toast.LENGTH_SHORT).show()

            //obtain the name and the address
            var name = txtName.text.toString()
            var description = txtDescription.text.toString()

            if ((name!="")&&(description!=null)) {
                //put the marker with the new position
                map?.addMarker(MarkerOptions().position(marcador))

                //save the place
                myDB.insertar(RegisterPlace(0, name, description, longitude, latitude))


                var alerts = AlertDialog.Builder(this)
                    alerts.setTitle("Desea visualizar los lugares registrados S/N ")
                    alerts.setMessage("Está seguro?")

               //OOOPSSSS doesn't work >|
                 alerts.setPositiveButton("ok", DialogInterface.OnClickListener { dialog, which ->
                    Toast.makeText(this, "Presiono OK", Toast.LENGTH_SHORT).show()


                    /* for (steps in myDB.listar()){
                         map?.addMarker(MarkerOptions().position(marcador))

                         //delete DB
                         myDB.eliminar(steps.id)
                     }*/

                })

                alerts.setNegativeButton("no", DialogInterface.OnClickListener { dialog, which ->
                    dialog.cancel()
                })
                alerts.show()
            }else{
                Toast.makeText(this, "Ingrese nombre y descripcion", Toast.LENGTH_SHORT).show()
            }
        }

        btnList.setOnClickListener {
            val fragmentList = ListPlacesFrag()
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.lyMap, fragmentList )
                .commit()
        }

    }





}
