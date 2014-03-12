% Initialization
% mass, position: (x, y, z), velocity: (x, y, z)
function [mass, xyzposition, xyzvelocity] = generate_body(n)
    % n = 0 simulates the solar system (sun and 9 planets)
    if nargin == 0
        n = 10;
        earth = 4;
        earthspeed = 29800;        
        % originalmass
        mass = [1.99e30; 400.30e23; 20.87e24; 5.97e24; 6.42e23; 1.90e27; 5.69e26; 1.02e26; 8.68e25; 1.31e22]; 
        % sunChangeTest, 1.99e30 - very interesting to look at the swings
        % try and change it to: 1.99e31, 1.99e32 after
        % Changing the sun mass by 2.5x
        sunChangeTest = [5e30; 400.30e23; 20.87e24; 5.97e24; 6.42e23; 1.90e27; 5.69e26; 1.02e26; 8.68e25; 1.31e22]; 
        % juiptermasstest, 1.90e27 -> change 1.90e30
        % Changing Jupiters mass by 10,000x
        juiptermasstest = [5e30; 400.30e23; 20.87e24; 5.97e24; 6.42e23; 1.90e31; 5.69e26; 1.02e26; 8.68e25; 1.31e22]; 
        dist = [0; 5.8e10; 1.08e11; 1.50e11; 2.28e11; 7.78e11; 1.43e12; 2.87e12; 4.50e12; 5.91e12];
        theta = 2 * pi * rand(10, 1);
        xyzposition = [dist.* cos(theta) dist.* sin(theta) zeros(10, 1)];
        speed = earthspeed * (dist(earth) ./ dist).^.5;
        speed(1) = 0;
        direction = [-xyzposition(:, 2) xyzposition(:, 1) zeros(10, 1)];
        direction = diag(1./sqrt(sum(direction.^2, 2))) * direction;
        direction(1,:) = [0 0 0];
        xyzvelocity = diag(speed) * direction;
        xyzvelocity(1, :) = [0 0 0];
    else
        % n > 0 generates n bodies at random to simulate
        % masses up to about 1/2 solar mass
        mass = 1e30 * rand(n,1);
        % coords up to about solar system radius
        dist = .5e13 * rand(n, 1);
        % random directions from origin
        theta = 2 * pi * rand(n, 1);
        % flattish disk
        xyzposition = [dist.* cos(theta) dist.* sin(theta) 1e11 * (rand(n, 1) -.5)];
        % initial velocities all zero
        xyzvelocity = zeros(n, 3);
    end;
end