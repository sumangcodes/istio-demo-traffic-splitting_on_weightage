# istio-demo-traffic-splitting_on_weightage

Here’s a sample `README.md` for your GitHub repository, explaining the purpose and usage of the provided Istio `VirtualService` configuration:

---

# Istio VirtualService Configuration for Service-1

This repository provides an Istio `VirtualService` configuration to manage traffic routing between two versions of `service-1` (v1 and v2) in the `sumanns` namespace.

## Overview

The `VirtualService` defined in this repository allows Istio to:
- Route incoming traffic directed to `/hello` to `service-1`.
- Split traffic between two versions of `service-1` based on weights:
  - 80% of traffic is routed to `service-1` version `v1`.
  - 20% of traffic is routed to `service-1` version `v2`.

This configuration is useful for gradual rollouts (canary releases), allowing a controlled percentage of requests to be handled by the new version (`v2`) while most traffic continues to use the stable version (`v1`).

## Configuration Details

### VirtualService Manifest

```yaml
apiVersion: networking.istio.io/v1alpha3
kind: VirtualService
metadata:
  name: service-1-virtualservice
  namespace: sumanns
spec:
  hosts:
  - "*"
  gateways:
  - service-1-gateway
  http:
  - match:
      - uri:
          exact: /hello
    route:
      - destination:
          host: service-1.sumanns.svc.cluster.local
          subset: v1
          port:
            number: 80
        weight: 80
      - destination:
          host: service-1.sumanns.svc.cluster.local
          subset: v2
          port:
            number: 80
        weight: 20
```

### Explanation of Key Sections

- **Hosts**: `["*"]` allows this virtual service to handle requests for all hosts via the configured gateway.
- **Gateways**: Specifies `service-1-gateway` as the Ingress Gateway for external traffic.
- **Match**: Routes requests matching the exact path `/hello` to `service-1`.
- **Route**:
  - **destination**: Points to `service-1` in the `sumanns` namespace.
  - **subsets**: Defines traffic destinations to specific versions (`v1` and `v2`) of `service-1`.
  - **weight**: Routes 80% of traffic to `v1` and 20% to `v2`, enabling gradual testing and monitoring of `v2`.

## Prerequisites

- Ensure Istio is installed and configured in your Kubernetes cluster.
- Ensure both versions (`v1` and `v2`) of `service-1` are deployed in the `sumanns` namespace.
- The `service-1-gateway` Ingress Gateway should be configured to allow external traffic to this service.

## Applying the Configuration

1. Deploy the VirtualService by running:
   ```bash
   kubectl apply -f service-1-virtualservice.yaml
   ```

2. Verify that the VirtualService has been created:
   ```bash
   kubectl get virtualservice -n sumanns
   ```

3. Test traffic routing by sending requests to `/hello` and observing that traffic is split between `v1` and `v2` according to the defined weights.

## Additional Notes

- **Traffic Splitting**: Adjust the `weight` values to control the percentage of traffic directed to each subset. For example, setting `weight: 50` for both versions would evenly split traffic.
- **Canary Testing**: This setup allows for easy canary testing of `v2` in a live environment with minimal risk.
- **Monitoring and Logs**: Use Istio's monitoring and logging tools to observe request metrics and ensure that `v2` is functioning as expected before increasing its traffic share.
