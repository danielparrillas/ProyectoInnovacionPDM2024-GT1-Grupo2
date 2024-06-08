import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.ues.proyectoinnovacion.R
import com.ues.proyectoinnovacion.api.marcacion.Marcacion
import java.text.SimpleDateFormat
import java.util.Locale

class MarcacionAdapter(context: Context, marcaciones: List<Marcacion>) :
    ArrayAdapter<Marcacion>(context, 0, marcaciones) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context)
            .inflate(R.layout.item_marcacion, parent, false)

        val marcacion = getItem(position)
        val partes = marcacion?.fecha?.split(" ")
        val hora = partes?.getOrNull(0) ?: ""
        val date = partes?.getOrNull(1) ?: ""
        val tipo = marcacion?.tipo?.uppercase()

        val tvTipo = view.findViewById<TextView>(R.id.tvTipo)
        val tvFecha = view.findViewById<TextView>(R.id.tvFecha)
        val tvHora = view.findViewById<TextView>(R.id.tvHora)

        tvTipo.text = tipo
        tvFecha.text = convertirFecha(date)
        tvHora.text = hora

        if (marcacion?.tipo?.equals("ENTRADA", ignoreCase = true) == true) {
            tvTipo.setBackgroundColor(ContextCompat.getColor(context, R.color.blue))
        } else if (marcacion?.tipo?.equals("SALIDA", ignoreCase = true) == true) {
            tvTipo.setBackgroundColor(ContextCompat.getColor(context, R.color.green))
        }

        return view
    }

    fun getMarcaciones(): List<Marcacion> {
        val marcaciones = mutableListOf<Marcacion>()
        for (i in 0 until count) {
            marcaciones.add(getItem(i)!!)
        }
        return marcaciones
    }

    private fun convertirFecha(fecha: String): String {
        val originalFormat = SimpleDateFormat("dd-MM-yyyy", Locale.US)
        val targetFormat = SimpleDateFormat("EEEE, dd 'de' MMMM 'de' yyyy", Locale("es", "ES"))
        val date = originalFormat.parse(fecha)
        return if (date != null) {
            targetFormat.format(date)
        } else {
            "Fecha no válida"
        }
    }
}