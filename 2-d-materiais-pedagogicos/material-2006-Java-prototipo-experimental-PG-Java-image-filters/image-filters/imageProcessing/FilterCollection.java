package imageProcessing;



public class FilterCollection {
	// Registered filters
	private static ImageFilter[] registeredFilters = {
		new ImageFilter(ImageFilter.F_BRIGHTNESS, "Brightness"),
		new ImageFilter(ImageFilter.F_GREYSCALE, "GreyScale"),
		new ImageFilter(ImageFilter.F_INVERT, "Inverter"),
		new ImageFilter(ImageFilter.F_PENCIL, "Pencil"),		
		new ImageFilter(ImageFilter.F_EMBOSS, "Emboss"),
		new ImageFilter(ImageFilter.F_NOISEREDUCTOR, "NoiseReductor"),
		new ImageFilter(ImageFilter.F_SHARPER, "Sharper")
		/* Adicionar novos filtros aqui */
	};
	
	public static ImageFilter[] getRegisteredFilters() {
		return registeredFilters;
	}
}
