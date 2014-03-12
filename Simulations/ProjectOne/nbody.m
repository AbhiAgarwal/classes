function n_body_array = nbody(n)
    clear all;
    close all;
    if nargin == 0 || n == 0
        n = 10;
        [mass, xyzposition, xyzpositionvelocity] = generate_body;
    else
        [mass, xyzposition, xyzpositionvelocity] = generate_body(n);
    end;
    G = 6.67e-11;
    hour = 60 * 60 * 24;
    dt = 1 * hour;
    steps = 1000;
    X_save = zeros(steps, 1);
    Y_save = zeros(steps, 1);
    clf reset;
    plot_pix(xyzposition, 0);

    for nOfSteps = 1:steps
        zeroArray = zeros(n, 3);
        for i = 1:n
            for j = 1:n
                dxyzposition = xyzposition(i, :) - xyzposition(j, :);
                r = norm(dxyzposition);
                if r == 0
                    continue
                end;
                f = G * mass(i) * mass(j) / r^2;
                fxyzposition = f * dxyzposition / r;
                zeroArray(i,:) = zeroArray(i,:) - fxyzposition / mass(i);
            end;
        end;
        % velocity
        xyzpositionvelocity = xyzpositionvelocity + dt * zeroArray;
        % position
        xyzposition = xyzposition + dt * xyzpositionvelocity
        plot_pix(xyzposition, nOfSteps); 
    end;
    n_body_array = xyzposition;
end

function plot_pix(v, t)
    dimOne = 5e12;
    dimTwo = 5e12;
    plot(v(:,1), v(:,2), 'b.', 'linewidth', 0.5); % planets
    %X_save(clock) = v(:,1);
    %Y_save(clock) = v(:,2);
    hold on;
    plot(v(1,1), v(1,2), 'y*', 'linewidth', 0.5); % sun
    axis([-dimOne dimOne -dimTwo dimTwo]);
    %axis square
    title(sprintf('Changes in orbit due to a change in Mass.'));
    hold off
    shg;
    pause(.02);
end
