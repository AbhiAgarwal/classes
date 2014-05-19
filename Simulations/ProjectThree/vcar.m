function v = vcar(d)
    global dmin dmax vmax;
    v = zeros(size(d));
    for k = 1:length(d)
        if (d(k) <= dmin)
            v(k) = 0;
        else
            v(k) = vmax * log(d(k) ./ dmin) / log(dmax / dmin) .* (d(k) > dmin) .* (d(k) < dmax) + vmax * (d(k) >= dmax);
        end
    end
end